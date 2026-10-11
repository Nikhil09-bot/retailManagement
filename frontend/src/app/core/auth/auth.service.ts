import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.model';

const STORAGE_KEY = 'retailbus.auth';

interface StoredAuth {
  authHeader: string;
  username: string;
}

/**
 * Backend uses HTTP Basic Auth (see SecurityConfig.java on the backend) -
 * there is no JWT. The Base64-encoded "Authorization" header value is kept
 * in memory (signal) and mirrored to sessionStorage so a page refresh
 * doesn't force a re-login.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly authState = signal<StoredAuth | null>(this.readStoredAuth());

  readonly isAuthenticated = computed(() => this.authState() !== null);
  readonly username = computed(() => this.authState()?.username ?? null);

  /** Header value to attach to outgoing requests, or null when logged out. */
  get authHeader(): string | null {
    return this.authState()?.authHeader ?? null;
  }

  async login(username: string, password: string): Promise<void> {
    const authHeader = 'Basic ' + btoa(`${username}:${password}`);
    const response = await firstValueFrom(
      this.http.get<ApiResponse<{ username: string }>>(`${environment.apiUrl}/auth/me`, {
        headers: { Authorization: authHeader },
      }),
    );
    const resolvedUsername = response.data?.username ?? username;
    const next: StoredAuth = { authHeader, username: resolvedUsername };
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify(next));
    this.authState.set(next);
  }

  async logout(): Promise<void> {
    // Clear local credentials FIRST: server-side logout cannot revoke a
    // Basic Authorization header the browser/app already holds.
    sessionStorage.removeItem(STORAGE_KEY);
    this.authState.set(null);
    try {
      await firstValueFrom(this.http.post(`${environment.apiUrl}/auth/logout`, {}));
    } catch {
      // Local logout must still succeed even if the API call fails.
    }
    this.router.navigate(['/login']);
  }

  private readStoredAuth(): StoredAuth | null {
    const raw = sessionStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as StoredAuth;
    } catch {
      return null;
    }
  }
}

