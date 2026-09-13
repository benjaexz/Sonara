import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface FavoriteResponse {
  id: string;
  track: {
    id: string;
    title: string;
    durationSeconds: number;
    artist: {
      id: string;
      name: string;
    };
    album: {
      id: string;
      title: string;
      releaseYear: number;
    };
    genre: {
      id: string;
      name: string;
    };
  };
}

@Injectable({
  providedIn: 'root',
})
export class Favorite {
  private readonly apiUrl = 'http://localhost:8080/favorites';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('sonara_token');

    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getFavorites(): Observable<FavoriteResponse[]> {
    return this.http.get<FavoriteResponse[]>(
      this.apiUrl,
      {
        headers: this.getHeaders()
      }
    );
  }

  addFavorite(trackId: string): Observable<FavoriteResponse> {
    return this.http.post<FavoriteResponse>(
      `${this.apiUrl}/${trackId}`,
      {},
      {
        headers: this.getHeaders()
      }
    );
  }

  removeFavorite(trackId: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${trackId}`,
      {
        headers: this.getHeaders()
      }
    );
  }
}