import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  private currentUserSubject = new BehaviorSubject<any>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap((res) => {
        if (res && res.token) {
          localStorage.setItem('sonara_token', res.token);

          // Extrai o nome/email das credenciais ou do payload JWT
          const username = credentials.email || credentials.username || 'User';
          const userObj = { username };

          localStorage.setItem('sonara_user', JSON.stringify(userObj));
          this.currentUserSubject.next(userObj);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('sonara_token');
    localStorage.removeItem('sonara_user');
    this.currentUserSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem('sonara_token');
  }

  private getUserFromStorage(): any {
    const user = localStorage.getItem('sonara_user');
    try {
      return user ? JSON.parse(user) : null;
    } catch {
      return null;
    }
  }
}