import { DecimalPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CustomerService } from '../customer.service';
import { Customer } from '../customer.model';

@Component({
  selector: 'app-customer-list',
  imports: [RouterLink, FormsModule, DecimalPipe],
  templateUrl: './customer-list.html',
  styleUrl: './customer-list.scss',
})
export class CustomerListComponent {
  private readonly service = inject(CustomerService);

  readonly customers = signal<Customer[]>([]);
  readonly loading = signal(true);
  keyword = '';

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (customers) => {
        this.customers.set(customers);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  search(): void {
    if (!this.keyword.trim()) {
      this.load();
      return;
    }
    this.service.search(this.keyword.trim()).subscribe((customers) => this.customers.set(customers));
  }

  remove(customer: Customer): void {
    if (!confirm(`Delete customer "${customer.fullName}"?`)) return;
    this.service.delete(customer.id).subscribe(() => this.load());
  }
}

