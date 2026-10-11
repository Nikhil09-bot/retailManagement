import { Component, inject } from '@angular/core';
import { IdleTimeoutService } from '../../../core/auth/idle-timeout.service';

@Component({
  selector: 'app-idle-timeout-modal',
  templateUrl: './idle-timeout-modal.html',
  styleUrl: './idle-timeout-modal.scss',
})
export class IdleTimeoutModalComponent {
  readonly idle = inject(IdleTimeoutService);

  continueSession(): void {
    this.idle.continueSession();
  }

  logout(): void {
    void this.idle.logoutNow();
  }
}

