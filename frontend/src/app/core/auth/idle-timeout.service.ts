import { Injectable, NgZone, OnDestroy, inject, signal } from '@angular/core';
import { AuthService } from './auth.service';

/** Change this one value to configure the global idle threshold. */
export const IDLE_TIMEOUT_MS = 10 * 60_000;

const ACTIVITY_EVENTS = ['mousemove', 'click', 'keydown', 'scroll'] as const;

/**
 * Singleton idle-detection timer, started/stopped by the authenticated
 * shell layout (see MainLayoutComponent). Not tied to any single route,
 * so it keeps a single running timer across page/route changes.
 */
@Injectable({ providedIn: 'root' })
export class IdleTimeoutService implements OnDestroy {
  private readonly auth = inject(AuthService);
  private readonly zone = inject(NgZone);

  readonly isPromptOpen = signal(false);

  private timerId: ReturnType<typeof setTimeout> | null = null;
  private listenersBound = false;
  private readonly boundRecordActivity = () => this.recordActivity();

  start(): void {
    if (this.listenersBound) return;
    this.listenersBound = true;

    // Run outside Angular's zone: activity listeners fire very often and
    // must not trigger change detection on every mouse move.
    this.zone.runOutsideAngular(() => {
      ACTIVITY_EVENTS.forEach((eventName) =>
        window.addEventListener(eventName, this.boundRecordActivity, { passive: true }),
      );
    });
    this.startTimer();
  }

  stop(): void {
    this.clearTimer();
    if (this.listenersBound) {
      ACTIVITY_EVENTS.forEach((eventName) =>
        window.removeEventListener(eventName, this.boundRecordActivity),
      );
      this.listenersBound = false;
    }
    this.isPromptOpen.set(false);
  }

  /** Called by the "Continue" button: dismiss the prompt and restart the clock. */
  continueSession(): void {
    this.isPromptOpen.set(false);
    this.startTimer();
  }

  /** Called by the "Logout" button (or auto-triggered choice). */
  async logoutNow(): Promise<void> {
    this.stop();
    await this.auth.logout();
  }

  private recordActivity(): void {
    // Deliberately ignore activity while the prompt is open; only the
    // explicit "Continue" action is allowed to restart the timer.
    if (!this.isPromptOpen()) {
      this.startTimer();
    }
  }

  private startTimer(): void {
    this.clearTimer();
    this.zone.runOutsideAngular(() => {
      this.timerId = setTimeout(() => {
        this.zone.run(() => this.isPromptOpen.set(true));
      }, IDLE_TIMEOUT_MS);
    });
  }

  private clearTimer(): void {
    if (this.timerId !== null) {
      clearTimeout(this.timerId);
      this.timerId = null;
    }
  }

  ngOnDestroy(): void {
    this.stop();
  }
}

