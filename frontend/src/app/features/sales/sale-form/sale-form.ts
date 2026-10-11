import { DecimalPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ProductService } from '../../products/product.service';
import { Product } from '../../products/product.model';
import { SaleService } from '../sale.service';

@Component({
  selector: 'app-sale-form',
  imports: [ReactiveFormsModule, RouterLink, DecimalPipe],
  templateUrl: './sale-form.html',
  styleUrl: './sale-form.scss',
})
export class SaleFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly productService = inject(ProductService);
  private readonly saleService = inject(SaleService);
  private readonly router = inject(Router);

  readonly products = signal<Product[]>([]);
  readonly saving = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    items: this.fb.array([this.createItemRow()]),
    taxRate: [0, [Validators.required, Validators.min(0)]],
    discountAmount: [0, [Validators.required, Validators.min(0)]],
    paymentMethod: ['CASH', Validators.required],
    notes: [''],
  });

  readonly estimatedSubtotal = computed(() => {
    const items = this.itemsValue();
    return items.reduce((sum, item) => {
      const product = this.products().find((p) => p.id === item.productId);
      const price = product?.sellingPrice ?? 0;
      return sum + price * (item.quantity || 0);
    }, 0);
  });

  private readonly itemsSignal = signal<{ productId: number | null; quantity: number }[]>([
    { productId: null, quantity: 1 },
  ]);

  constructor() {
    this.productService.getAll().subscribe((products) => this.products.set(products.filter((p) => p.active)));
    this.itemsArray.valueChanges.subscribe((value) => this.itemsSignal.set(value));
  }

  private itemsValue() {
    return this.itemsSignal();
  }

  get itemsArray(): FormArray {
    return this.form.controls.items;
  }

  private createItemRow() {
    return this.fb.nonNullable.group({
      productId: [null as number | null, Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
    });
  }

  addItem(): void {
    this.itemsArray.push(this.createItemRow());
  }

  removeItem(index: number): void {
    if (this.itemsArray.length === 1) return;
    this.itemsArray.removeAt(index);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.errorMessage.set(null);
    this.saving.set(true);
    const raw = this.form.getRawValue();

    const request = {
      items: raw.items.map((item) => ({
        productId: item.productId as number,
        quantity: item.quantity,
      })),
      taxRate: raw.taxRate,
      discountAmount: raw.discountAmount,
      paymentMethod: raw.paymentMethod,
      notes: raw.notes,
    };

    this.saleService.create(request).subscribe({
      next: () => this.router.navigate(['/sales']),
      error: () => {
        this.errorMessage.set('Unable to record sale. Check stock levels and try again.');
        this.saving.set(false);
      },
    });
  }
}

