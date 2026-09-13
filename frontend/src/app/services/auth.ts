import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

export interface LoginRequest {
  email: string;
  password?: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password?: string;
}

export interface AuthResponse {
  token: string;
  username?: string;
  name?: string;
}

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private readonly apiUrl = '/api/auth';
  private readonly tokenKey = 'auth_token';
  private readonly userKey = 'auth_user';

  private currentUserSubject = new BehaviorSubject<string | null>(this.getStoredUsername());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) { }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials);
  }

  register(data: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data);
  }

  saveToken(token: string, username?: string): void {
    localStorage.setItem(this.tokenKey, token);
    const finalUsername = username || this.parseTokenUsername(token) || 'Usuário';
    localStorage.setItem(this.userKey, finalUsername);
    this.currentUserSubject.next(finalUsername);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.currentUserSubject.next(null);
  }

  private getStoredUsername(): string | null {
    return localStorage.getItem(this.userKey);
  }

  private parseTokenUsername(token: string): string | null {
    try {
      const parts = token.split('.');
      if (parts.length < 2) return null;

      const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      const payload = JSON.parse(decodeURIComponent(escape(atob(base64))));

      return payload.username || payload.name || payload.sub || null;
    } catch {
      return null;
    }
  }
}