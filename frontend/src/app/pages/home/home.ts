import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Track, TrackResponse } from '../../services/track';
import { Favorite, FavoriteResponse } from '../../services/favorite';
import { Playlist, PlaylistResponse } from '../../services/playlist';
import { HistoryService } from '../../services/history';
import { PlayerService } from '../../services/player';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  tracks: TrackResponse[] = [];
  favorites: FavoriteResponse[] = [];
  playlists: PlaylistResponse[] = [];

  selectedPlaylists: { [trackId: string]: string } = {};

  errorMessage = '';
  successMessage = '';

  constructor(
    private trackService: Track,
    private favoriteService: Favorite,
    private playlistService: Playlist,
    private historyService: HistoryService,
    private playerService: PlayerService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadTracks();
    this.loadFavorites();
    this.loadPlaylists();
  }

  loadTracks(): void {
    this.trackService.getTracks().subscribe({
      next: (response) => {
        this.tracks = response;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar músicas. Faça login novamente.';
        this.cdr.detectChanges();
      }
    });
  }

  loadFavorites(): void {
    this.favoriteService.getFavorites().subscribe({
      next: (response) => {
        this.favorites = response;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar favoritos.';
        this.cdr.detectChanges();
      }
    });
  }

  loadPlaylists(): void {
    this.playlistService.getPlaylists().subscribe({
      next: (response) => {
        this.playlists = response;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Erro ao carregar playlists.';
        this.cdr.detectChanges();
      }
    });
  }

  playTrack(track: TrackResponse): void {
    this.playerService.play(track);

    this.historyService.registerListening(track.id).subscribe({
      next: () => {
        this.errorMessage = '';
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Erro ao registrar reprodução no histórico.';
        this.cdr.detectChanges();
      }
    });
  }

  isFavorite(trackId: string): boolean {
    return this.favorites.some(favorite => favorite.track.id === trackId);
  }

  toggleFavorite(trackId: string): void {
    if (this.isFavorite(trackId)) {
      this.favoriteService.removeFavorite(trackId).subscribe({
        next: () => {
          this.loadFavorites();
        },
        error: () => {
          this.errorMessage = 'Erro ao remover favorito.';
          this.cdr.detectChanges();
        }
      });
      return;
    }

    this.favoriteService.addFavorite(trackId).subscribe({
      next: () => {
        this.loadFavorites();
      },
      error: () => {
        this.errorMessage = 'Erro ao adicionar favorito.';
        this.cdr.detectChanges();
      }
    });
  }

  addToPlaylist(trackId: string): void {
    const playlistId = this.selectedPlaylists[trackId];

    if (!playlistId) {
      this.errorMessage = 'Selecione uma playlist.';
      this.successMessage = '';
      this.cdr.detectChanges();
      return;
    }

    this.playlistService.addTrackToPlaylist(playlistId, trackId).subscribe({
      next: () => {
        this.errorMessage = '';
        this.successMessage = 'Música adicionada à playlist.';
        this.cdr.detectChanges();
      },
      error: () => {
        this.successMessage = '';
        this.errorMessage = 'Erro ao adicionar música à playlist.';
        this.cdr.detectChanges();
      }
    });
  }
}