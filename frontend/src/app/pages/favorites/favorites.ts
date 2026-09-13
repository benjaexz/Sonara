import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Favorite, FavoriteResponse } from '../../services/favorite';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './favorites.html',
  styleUrl: './favorites.css',
})
export class Favorites implements OnInit {
  favorites: FavoriteResponse[] = [];
  errorMessage = '';

  constructor(
    private favoriteService: Favorite,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.favoriteService.getFavorites().subscribe({
      next: (response) => {
        this.favorites = response;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erro ao carregar favoritos:', error);
        this.errorMessage = 'Erro ao carregar músicas curtidas.';
        this.cdr.detectChanges();
      }
    });
  }

  formatDuration(seconds?: number): string {
    if (!seconds || seconds <= 0) return '0:00';
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
  }
}