import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, map, Observable, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = environment.apiUrl + '/auth';
  private refreshTokenTimeout: any;

  constructor(private http: HttpClient, private router: Router) { }

  login(credentials: Credentials): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, credentials).pipe(
      map(response => {
        if (response && response.token && response.refreshToken) {
          this.setSession(response);
        }
        return response;
      })
    );
  }

  register(user: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/register`, user);
  }

  logout(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  refreshToken(): Observable<LoginResponse> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) {
      this.logout();
      return throwError(() => new Error('No refresh token available'));
    }

    return this.http.post<LoginResponse>(`${this.baseUrl}/refresh-token`, { refreshToken }).pipe(
      tap(response => {
        if (response && response.token && response.refreshToken) {
          this.setSession(response);
        }
      }),
      catchError((error: HttpErrorResponse) => {
        this.logout();
        return throwError(() => error);
      })
    );
  }

  private setSession(response: LoginResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('refreshToken', response.refreshToken);
    localStorage.setItem('expiresIn', response.expiresIn / 1000 + '')
    this.scheduleTokenRefresh();
  }

  private clearSession(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    clearTimeout(this.refreshTokenTimeout);
  }

  private getExpiresIn(): number {
    return parseInt(localStorage.getItem('expiresIn') || '0', 10);
  }

  private scheduleTokenRefresh(): void {
    const expiresIn = this.getExpiresIn();
    this.refreshTokenTimeout = setTimeout(() => {
      this.refreshToken().subscribe();
    }, (expiresIn - 60) * 1000);
  }

}

interface LoginResponse {
  token: string;
  expiresIn: number;
  refreshToken: string;
}

interface Credentials {
  username: string;
  password: string;
}
