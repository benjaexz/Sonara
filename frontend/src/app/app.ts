import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, RouterModule } from '@angular/router';
import { Auth } from './services/auth';
import { Player } from './components/player/player'; // ou o caminho do seu PlayerComponent

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule, Player],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  private auth = inject(Auth);
  private router = inject(Router);

  username: string = '';
  currentUser$ = this.auth.currentUser$;

  constructor() {
    this.currentUser$.subscribe((user: any) => {
      this.username = user?.username || user?.name || '';
    });
  }

  isAuthRoute(): boolean {
    const currentUrl = this.router.url;
    return currentUrl.includes('/login') || currentUrl.includes('/register');
  }

  logout(): void {
    this.auth.logout();
  }
}