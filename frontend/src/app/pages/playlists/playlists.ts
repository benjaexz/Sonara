import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Playlist, PlaylistResponse } from '../../services/playlist';

@Component({
  selector: 'app-playlists',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './playlists.html',
  styleUrl: './playlists.css'
})
export class Playlists implements OnInit {

  playlists: PlaylistResponse[] = [];

  name = '';
  description = '';
  errorMessage = '';

  constructor(
    private playlistService: Playlist,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadPlaylists();
  }

  loadPlaylists(): void {
    this.playlistService.getPlaylists().subscribe({
      next: (response) => {
        this.playlists = response;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = 'Erro ao carregar playlists.';
        this.cdr.detectChanges();
      }
    });
  }

  createPlaylist(): void {
    if (!this.name.trim()) {
      this.errorMessage = 'Nome da playlist é obrigatório.';
      return;
    }

    this.playlistService.createPlaylist({
      name: this.name,
      description: this.description
    }).subscribe({
      next: () => {
        this.name = '';
        this.description = '';
        this.errorMessage = '';
        this.loadPlaylists();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = 'Erro ao criar playlist.';
        this.cdr.detectChanges();
      }
    });
  }

  deletePlaylist(playlistId: string): void {
    this.playlistService.deletePlaylist(playlistId).subscribe({
      next: () => {
        this.loadPlaylists();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = 'Erro ao remover playlist.';
        this.cdr.detectChanges();
      }
    });
  }

  removeTrack(playlistId: string, trackId: string): void {
    this.playlistService.removeTrackFromPlaylist(playlistId, trackId).subscribe({
      next: () => {
        this.loadPlaylists();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = 'Erro ao remover faixa da playlist.';
        this.cdr.detectChanges();
      }
    });
  }
}