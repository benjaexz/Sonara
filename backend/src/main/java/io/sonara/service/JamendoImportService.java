package io.sonara.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sonara.entity.Album;
import io.sonara.entity.Artist;
import io.sonara.entity.Genre;
import io.sonara.entity.Track;
import io.sonara.repository.AlbumRepository;
import io.sonara.repository.ArtistRepository;
import io.sonara.repository.GenreRepository;
import io.sonara.repository.TrackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Service
public class JamendoImportService {

    private static final Logger log = LoggerFactory.getLogger(JamendoImportService.class);
    private static final String TRACKS_URL = "https://api.jamendo.com/v3.0/tracks/";
    private static final String SOURCE_PREFIX = "jamendo:";

    private enum Result { IMPORTED, UPDATED, SKIPPED }

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${JAMENDO_CLIENT_ID:}")
    private String clientId;

    public JamendoImportService(
            TrackRepository trackRepository,
            ArtistRepository artistRepository,
            AlbumRepository albumRepository,
            GenreRepository genreRepository,
            ObjectMapper objectMapper
    ) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
        this.objectMapper = objectMapper;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(20000);
        this.restTemplate = new RestTemplate(factory);
    }

    public Map<String, Integer> importTracks(int limit, int offset, String order, String tags) {

        if (clientId == null || clientId.isBlank()) {
            throw new IllegalStateException("JAMENDO_CLIENT_ID is not configured");
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(TRACKS_URL)
                .queryParam("client_id", clientId)
                .queryParam("format", "json")
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("order", order)
                .queryParam("include", "musicinfo");

        if (tags != null && !tags.isBlank()) {
            builder.queryParam("tags", tags);
        }

        URI uri = builder.build().encode().toUri();

        JsonNode root;
        try {
            root = objectMapper.readTree(restTemplate.getForObject(uri, String.class));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to call Jamendo API", e);
        }

        JsonNode headers = root.path("headers");
        if (!"success".equals(headers.path("status").asText())) {
            throw new IllegalStateException("Jamendo API error: " + headers.path("error_message").asText());
        }

        int imported = 0;
        int updated = 0;
        int skipped = 0;
        int failed = 0;

        for (JsonNode item : root.path("results")) {
            try {
                Result result = importOne(item);

                if (result == Result.IMPORTED) {
                    imported++;
                } else if (result == Result.UPDATED) {
                    updated++;
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                failed++;
                log.warn("Failed to import Jamendo track {}: {}", item.path("id").asText(), e.getMessage());
            }
        }

        return Map.of(
                "imported", imported,
                "updated", updated,
                "skipped", skipped,
                "failed", failed
        );
    }

    private Result importOne(JsonNode item) {

        String jamendoId = item.path("id").asText("");
        String title = clean(item.path("name").asText(""), 255);
        String artistName = clean(item.path("artist_name").asText(""), 255);
        String audio = item.path("audio").asText("");
        String cover = item.path("album_image").asText(item.path("image").asText(""));

        if (jamendoId.isBlank() || title.isBlank() || artistName.isBlank() || audio.isBlank()) {
            return Result.SKIPPED;
        }

        String externalId = SOURCE_PREFIX + jamendoId;
        String coverUrl = cover.isBlank() ? null : cover;

        Optional<Track> existing = trackRepository.findByExternalId(externalId);

        if (existing.isPresent()) {
            Track existingTrack = existing.get();
            existingTrack.setAudioUrl(audio);
            existingTrack.setCoverUrl(coverUrl);
            trackRepository.save(existingTrack);
            return Result.UPDATED;
        }

        Artist artist = artistRepository.findByName(artistName).orElseGet(() -> {
            Artist newArtist = new Artist();
            newArtist.setName(artistName);
            return artistRepository.save(newArtist);
        });

        Album album = null;
        String albumName = clean(item.path("album_name").asText(""), 255);

        if (!albumName.isBlank()) {
            album = albumRepository.findFirstByTitleAndArtist(albumName, artist).orElseGet(() -> {
                Album newAlbum = new Album();
                newAlbum.setTitle(albumName);
                newAlbum.setReleaseYear(parseYear(item.path("releasedate").asText("")));
                newAlbum.setArtist(artist);
                return albumRepository.save(newAlbum);
            });
        }

        Genre genre = null;
        JsonNode genres = item.path("musicinfo").path("tags").path("genres");

        if (genres.isArray() && genres.size() > 0) {
            String genreName = clean(genres.get(0).asText(""), 50);

            if (!genreName.isBlank()) {
                genre = genreRepository.findByName(genreName).orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName(genreName);
                    return genreRepository.save(newGenre);
                });
            }
        }

        Track track = new Track(
                title,
                item.path("duration").asInt(0),
                artist,
                album,
                genre
        );

        track.setAudioUrl(audio);
        track.setCoverUrl(coverUrl);
        track.setExternalId(externalId);

        trackRepository.save(track);

        return Result.IMPORTED;
    }

    private String clean(String value, int maxLength) {
        String v = value == null ? "" : value.trim();
        return v.length() > maxLength ? v.substring(0, maxLength) : v;
    }

    private Integer parseYear(String date) {
        if (date == null || date.length() < 4) {
            return null;
        }

        try {
            return Integer.parseInt(date.substring(0, 4));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}