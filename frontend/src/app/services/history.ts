import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ArtistResponse {
    id: string;
    name: string;
}

export interface AlbumResponse {
    id: string;
    title: string;
    releaseYear?: number;
    artist?: ArtistResponse;
}

export interface GenreResponse {
    id: string;
    name: string;
}

export interface TrackResponse {
    id: string;
    title: string;
    durationSeconds?: number;
    artist?: ArtistResponse;
    album?: AlbumResponse;
    genre?: GenreResponse;
}

export interface HistoryItemResponse {
    id: string;
    track: TrackResponse;
    listenedAt: string;
}

@Injectable({
    providedIn: 'root'
})
export class HistoryService {

    private readonly apiUrl = 'http://localhost:8080/history';

    constructor(private http: HttpClient) { }

    private getAuthHeaders(): HttpHeaders {
        const token = localStorage.getItem('sonara_token');
        return new HttpHeaders({
            Authorization: `Bearer ${token}`
        });
    }

    getHistory(): Observable<HistoryItemResponse[]> {
        return this.http.get<HistoryItemResponse[]>(this.apiUrl, {
            headers: this.getAuthHeaders()
        });
    }

    registerListening(trackId: string): Observable<void> {
        return this.http.post<void>(
            `${this.apiUrl}/${trackId}`,
            {},
            {
                headers: this.getAuthHeaders()
            }
        );
    }
}