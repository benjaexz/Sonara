import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PlayerService } from '../../services/player';

@Component({
    selector: 'app-player',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './player.html',
    styleUrl: './player.css'
})
export class Player {
    private playerService = inject(PlayerService);

    currentTrack$ = this.playerService.currentTrack$;
    isPlaying$ = this.playerService.isPlaying$;

    progress = 30;
    volume = 80;

    togglePlay(): void {
        this.playerService.togglePlay();
    }
}