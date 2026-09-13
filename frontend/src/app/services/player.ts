import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { TrackResponse } from './track';

@Injectable({
    providedIn: 'root'
})
export class PlayerService {
    private currentTrackSubject = new BehaviorSubject<TrackResponse | null>(null);
    currentTrack$ = this.currentTrackSubject.asObservable();

    private isPlayingSubject = new BehaviorSubject<boolean>(false);
    isPlaying$ = this.isPlayingSubject.asObservable();

    play(track: TrackResponse): void {
        this.currentTrackSubject.next(track);
        this.isPlayingSubject.next(true);
    }

    togglePlay(): void {
        if (!this.currentTrackSubject.value) return;
        this.isPlayingSubject.next(!this.isPlayingSubject.value);
    }

    stop(): void {
        this.isPlayingSubject.next(false);
    }
}