import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { DashboardService } from './dashboard.service';
import { DashboardStats } from './dashboard.model';

@Component({
  selector: 'app-dashboard',
  imports: [DecimalPipe],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class DashboardComponent {
  private readonly service = inject(DashboardService);

  readonly stats = signal<DashboardStats | null>(null);
  readonly loading = signal(true);

  constructor() {
    this.service.getStats().subscribe({
      next: (stats) => {
        this.stats.set(stats);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}


