import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { Profile } from '../interfaces/Profile';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = environment.baseUrl + '/auth';
  private userUrl = environment.baseUrl + '/api/users';
  private refreshTokenTimeout: any;
  private isRefreshing = false;
  private tokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  public isAuthenticated$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(this.isAuthenticated());
  public userProfile$: BehaviorSubject<Profile | null> = new BehaviorSubject<Profile | null>(null);

  constructor(private http: HttpClient, private router: Router) { }

  login(credentials: Credentials): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.authUrl}/login`, credentials).pipe(
      map(response => {
        if (response && response.token && response.refreshToken) {
          this.setSession(response);
          this.isAuthenticated$.next(true);
          this.getUserProfile();
        }
        return response;
      })
    );
  }

  register(user: any): Observable<any> {
    return this.http.post<any>(`${this.authUrl}/register`, user);
  }

  logout(): void {
    this.clearSession();
    this.isAuthenticated$.next(false);
    this.userProfile$.next(null);
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getProfile(): Observable<Profile> {
    return this.http.get<Profile>(`${this.userUrl}/profile`);
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

    if (this.isRefreshing) {
      return this.tokenSubject.pipe(
        switchMap(token => {
          if (token) {
            return this.http.post<LoginResponse>(`${this.authUrl}/refresh-token`, { refreshToken });
          }
          return throwError(() => new Error('Token refresh failed'));
        })
      );
    } else {
      this.isRefreshing = true;
      this.tokenSubject.next(null);

      return this.http.post<LoginResponse>(`${this.authUrl}/refresh-token`, { refreshToken }).pipe(
        tap(response => {
          if (response && response.token && response.refreshToken) {
            this.setSession(response);
            this.tokenSubject.next(response.token);
            this.isAuthenticated$.next(true);
          }
        }),
        catchError((error: HttpErrorResponse) => {
          this.logout();
          return throwError(() => error);
        }),
        tap(() => {
          this.isRefreshing = false;
        })
      );
    }
  }

  private setSession(response: LoginResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('refreshToken', response.refreshToken);
    localStorage.setItem('expiresIn', response.expiresIn / 1000 + '');
    this.scheduleTokenRefresh();
    this.getUserProfile();
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

  private getUserProfile(): void {
    this.getProfile().pipe(
      tap(profile => this.userProfile$.next(profile)),
      catchError(error => {
        console.error('Error fetching user profile:', error);
        return throwError(() => error);
      })
    ).subscribe();
  }
}

interface LoginResponse {
  token: string;
  expiresIn: number;
  refreshToken: string;
}

export interface Credentials {
  username: string;
  password: string;
}