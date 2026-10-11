import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SaleService } from '../sale.service';
import { Sale } from '../sale.model';

@Component({
  selector: 'app-sale-list',
  imports: [RouterLink, DecimalPipe, DatePipe],
  templateUrl: './sale-list.html',
  styleUrl: './sale-list.scss',
})
export class SaleListComponent {
  private readonly service = inject(SaleService);

  readonly sales = signal<Sale[]>([]);
  readonly loading = signal(true);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (sales) => {
        this.sales.set(sales);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  cancel(sale: Sale): void {
    if (!confirm(`Cancel sale ${sale.receiptNumber}?`)) return;
    this.service.cancel(sale.id).subscribe(() => this.load());
  }

  downloadInvoice(sale: Sale): void {
    this.service.downloadInvoicePdf(sale.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `invoice-${sale.receiptNumber}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to download invoice PDF.'),
    });
  }
}


