import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, RouterModule } from '@angular/router';
import { Auth } from './services/auth';
import { Player } from './components/player/player';

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

  username: string = 'User';
  currentUser$ = this.auth.currentUser$;

  constructor() {
    this.currentUser$.subscribe((user: any) => {
      if (typeof user === 'string') {
        this.username = user;
      } else if (user && typeof user === 'object') {
        this.username = user.username || user.name || user.email || 'User';
      } else {
        this.username = 'User';
      }
    });
  }

  isAuthRoute(): boolean {
    const currentUrl = this.router.url;
    return currentUrl.includes('/login') || currentUrl.includes('/register');
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}