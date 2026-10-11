import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProductService } from '../product.service';
import { Product } from '../product.model';

@Component({
  selector: 'app-product-list',
  imports: [RouterLink, FormsModule, DecimalPipe],
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss',
})
export class ProductListComponent {
  private readonly service = inject(ProductService);

  readonly products = signal<Product[]>([]);
  readonly loading = signal(true);
  keyword = '';

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (products) => {
        this.products.set(products);
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
    this.service.search(this.keyword.trim()).subscribe((products) => this.products.set(products));
  }

  disable(product: Product): void {
    if (!confirm(`Disable product "${product.name}"?`)) return;
    this.service.disable(product.id).subscribe(() => this.load());
  }

  remove(product: Product): void {
    if (!confirm(`Delete product "${product.name}"? This cannot be undone.`)) return;
    this.service.delete(product.id).subscribe(() => this.load());
  }
}


