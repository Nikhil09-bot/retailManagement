import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PurchaseOrderService } from '../purchase-order.service';
import { PurchaseOrder } from '../purchase-order.model';

@Component({
  selector: 'app-purchase-order-list',
  imports: [RouterLink, DecimalPipe, DatePipe],
  templateUrl: './purchase-order-list.html',
  styleUrl: './purchase-order-list.scss',
})
export class PurchaseOrderListComponent {
  private readonly service = inject(PurchaseOrderService);

  readonly orders = signal<PurchaseOrder[]>([]);
  readonly loading = signal(true);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (orders) => {
        this.orders.set(orders);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  receive(order: PurchaseOrder): void {
    if (!confirm(`Mark PO ${order.poNumber} as received?`)) return;
    this.service.receive(order.id).subscribe(() => this.load());
  }

  cancel(order: PurchaseOrder): void {
    if (!confirm(`Cancel PO ${order.poNumber}?`)) return;
    this.service.cancel(order.id).subscribe(() => this.load());
  }

  remove(order: PurchaseOrder): void {
    if (!confirm(`Delete PO ${order.poNumber}?`)) return;
    this.service.delete(order.id).subscribe(() => this.load());
  }
}

