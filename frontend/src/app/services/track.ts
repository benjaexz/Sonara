import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TrackResponse {
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
}

@Injectable({
  providedIn: 'root',
})
export class Track {
  private readonly apiUrl = 'http://localhost:8080/tracks';

  constructor(private http: HttpClient) { }

  getTracks(): Observable<TrackResponse[]> {
    const token = localStorage.getItem('sonara_token');

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<TrackResponse[]>(this.apiUrl, {
      headers
    });
  }
}