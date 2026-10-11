import { DecimalPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ReportsService } from './reports.service';
import { SalesReport, TopSellingProduct } from './reports.model';

@Component({
  selector: 'app-reports-page',
  imports: [DecimalPipe],
  templateUrl: './reports-page.html',
  styleUrl: './reports-page.scss',
})
export class ReportsPageComponent {
  private readonly service = inject(ReportsService);

  readonly report = signal<SalesReport | null>(null);
  readonly topProducts = signal<TopSellingProduct[]>([]);
  readonly loading = signal(true);

  constructor() {
    const now = new Date();
    const month = now.getMonth() + 1;
    const year = now.getFullYear();

    this.service.getMonthlySales(month, year).subscribe({
      next: (report) => {
        this.report.set(report);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });

    const start = new Date(year, month - 1, 1).toISOString().slice(0, 10);
    const end = new Date(year, month, 0).toISOString().slice(0, 10);
    this.service.getTopProducts(start, end, 5).subscribe((products) => this.topProducts.set(products));
  }
}


