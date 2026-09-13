import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Favorites } from './pages/favorites/favorites';
import { Playlists } from './pages/playlists/playlists';
import { History } from './pages/history/history';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
    { path: '', component: Home },
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: 'favorites', component: Favorites, canActivate: [authGuard] },
    { path: 'playlists', component: Playlists, canActivate: [authGuard] },
    { path: 'history', component: History, canActivate: [authGuard] }
];