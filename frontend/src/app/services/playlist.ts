import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PlaylistResponse {
  id: string;
  name: string;
  description: string;
  tracks: any[];
}

export interface PlaylistRequest {
  name: string;
  description: string;
}

@Injectable({
  providedIn: 'root'
})
export class Playlist {

  private readonly apiUrl = 'http://localhost:8080/playlists';

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {

    const token = localStorage.getItem('sonara_token');

    console.log('TOKEN PLAYLIST:', token);

    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getPlaylists(): Observable<PlaylistResponse[]> {
    return this.http.get<PlaylistResponse[]>(
      this.apiUrl,
      {
        headers: this.getAuthHeaders()
      }
    );
  }

  createPlaylist(request: PlaylistRequest): Observable<PlaylistResponse> {
    return this.http.post<PlaylistResponse>(
      this.apiUrl,
      request,
      {
        headers: this.getAuthHeaders()
      }
    );
  }

  deletePlaylist(playlistId: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${playlistId}`,
      {
        headers: this.getAuthHeaders()
      }
    );
  }

  addTrackToPlaylist(
    playlistId: string,
    trackId: string
  ): Observable<PlaylistResponse> {
    return this.http.post<PlaylistResponse>(
      `${this.apiUrl}/${playlistId}/tracks/${trackId}`,
      {},
      {
        headers: this.getAuthHeaders()
      }
    );
  }

  removeTrackFromPlaylist(
    playlistId: string,
    trackId: string
  ): Observable<PlaylistResponse> {
    return this.http.delete<PlaylistResponse>(
      `${this.apiUrl}/${playlistId}/tracks/${trackId}`,
      {
        headers: this.getAuthHeaders()
      }
    );
  }
}