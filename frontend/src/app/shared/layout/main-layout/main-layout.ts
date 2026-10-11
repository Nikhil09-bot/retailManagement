import { Component, effect, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { IdleTimeoutService } from '../../../core/auth/idle-timeout.service';
import { IdleTimeoutModalComponent } from '../../components/idle-timeout-modal/idle-timeout-modal';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, IdleTimeoutModalComponent],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.scss',
})
export class MainLayoutComponent {
  readonly auth = inject(AuthService);
  private readonly idle = inject(IdleTimeoutService);

  readonly navLinks = [
    { path: '/dashboard', label: 'Dashboard' },
    { path: '/products', label: 'Products' },
    { path: '/customers', label: 'Customers' },
    { path: '/suppliers', label: 'Suppliers' },
    { path: '/sales', label: 'Sales' },
    { path: '/inventory', label: 'Inventory' },
    { path: '/purchase-orders', label: 'Purchase Orders' },
    { path: '/reports', label: 'Reports' },
  ];

  constructor() {
    // Idle timer only runs while an authenticated shell is actually mounted.
    effect((onCleanup) => {
      this.idle.start();
      onCleanup(() => this.idle.stop());
    });
  }

  logout(): void {
    void this.auth.logout();
  }
}

