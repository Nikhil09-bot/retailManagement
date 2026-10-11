import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../products/product.service';
import { Product } from '../products/product.model';
import { InventoryService } from './inventory.service';
import { LowStockProduct } from './inventory.model';

@Component({
  selector: 'app-inventory-page',
  imports: [ReactiveFormsModule],
  templateUrl: './inventory-page.html',
  styleUrl: './inventory-page.scss',
})
export class InventoryPageComponent {
  private readonly fb = inject(FormBuilder);
  private readonly inventoryService = inject(InventoryService);
  private readonly productService = inject(ProductService);

  readonly lowStock = signal<LowStockProduct[]>([]);
  readonly outOfStock = signal<Product[]>([]);
  readonly products = signal<Product[]>([]);
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly successMessage = signal<string | null>(null);
  readonly errorMessage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    productId: [null as number | null, Validators.required],
    adjustmentQuantity: [0, Validators.required],
    reason: ['', Validators.required],
    notes: [''],
  });

  constructor() {
    this.load();
    this.productService.getAll().subscribe((products) => this.products.set(products));
  }

  load(): void {
    this.loading.set(true);
    this.inventoryService.getLowStock().subscribe((data) => this.lowStock.set(data));
    this.inventoryService.getOutOfStock().subscribe({
      next: (data) => {
        this.outOfStock.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  submitAdjustment(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.successMessage.set(null);
    this.errorMessage.set(null);
    this.saving.set(true);
    const raw = this.form.getRawValue();

    this.inventoryService
      .adjustStock({
        productId: raw.productId as number,
        adjustmentQuantity: raw.adjustmentQuantity,
        reason: raw.reason,
        notes: raw.notes,
      })
      .subscribe({
        next: () => {
          this.successMessage.set('Stock adjusted successfully.');
          this.saving.set(false);
          this.form.reset({ productId: null, adjustmentQuantity: 0, reason: '', notes: '' });
          this.load();
        },
        error: () => {
          this.errorMessage.set('Unable to adjust stock.');
          this.saving.set(false);
        },
      });
  }
}

