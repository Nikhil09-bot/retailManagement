import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SupplierService } from '../supplier.service';
import { Supplier } from '../supplier.model';

@Component({
  selector: 'app-supplier-list',
  imports: [RouterLink, FormsModule],
  templateUrl: './supplier-list.html',
  styleUrl: './supplier-list.scss',
})
export class SupplierListComponent {
  private readonly service = inject(SupplierService);

  readonly suppliers = signal<Supplier[]>([]);
  readonly loading = signal(true);
  keyword = '';

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (suppliers) => {
        this.suppliers.set(suppliers);
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
    this.service.search(this.keyword.trim()).subscribe((suppliers) => this.suppliers.set(suppliers));
  }

  deactivate(supplier: Supplier): void {
    if (!confirm(`Deactivate supplier "${supplier.name}"?`)) return;
    this.service.deactivate(supplier.id).subscribe(() => this.load());
  }

  remove(supplier: Supplier): void {
    if (!confirm(`Delete supplier "${supplier.name}"?`)) return;
    this.service.delete(supplier.id).subscribe(() => this.load());
  }
}

