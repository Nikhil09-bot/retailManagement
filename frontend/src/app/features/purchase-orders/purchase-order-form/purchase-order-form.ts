import { Component, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ProductService } from '../../products/product.service';
import { Product } from '../../products/product.model';
import { SupplierService } from '../../suppliers/supplier.service';
import { Supplier } from '../../suppliers/supplier.model';
import { PurchaseOrderService } from '../purchase-order.service';

@Component({
  selector: 'app-purchase-order-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './purchase-order-form.html',
  styleUrl: './purchase-order-form.scss',
})
export class PurchaseOrderFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly productService = inject(ProductService);
  private readonly supplierService = inject(SupplierService);
  private readonly poService = inject(PurchaseOrderService);
  private readonly router = inject(Router);

  readonly products = signal<Product[]>([]);
  readonly suppliers = signal<Supplier[]>([]);
  readonly saving = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    supplierId: [null as number | null, Validators.required],
    expectedDeliveryDate: [''],
    notes: [''],
    items: this.fb.array([this.createItemRow()]),
  });

  constructor() {
    this.productService.getAll().subscribe((products) => this.products.set(products.filter((p) => p.active)));
    this.supplierService.getAll().subscribe((suppliers) => this.suppliers.set(suppliers.filter((s) => s.active)));
  }

  get itemsArray(): FormArray {
    return this.form.controls.items;
  }

  private createItemRow() {
    return this.fb.nonNullable.group({
      productId: [null as number | null, Validators.required],
      quantityOrdered: [1, [Validators.required, Validators.min(1)]],
      unitPrice: [0, [Validators.required, Validators.min(0)]],
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

    this.poService
      .create({
        supplierId: raw.supplierId as number,
        expectedDeliveryDate: raw.expectedDeliveryDate || null,
        notes: raw.notes,
        items: raw.items.map((item) => ({
          productId: item.productId as number,
          quantityOrdered: item.quantityOrdered,
          unitPrice: item.unitPrice,
        })),
      })
      .subscribe({
        next: () => this.router.navigate(['/purchase-orders']),
        error: () => {
          this.errorMessage.set('Unable to create purchase order.');
          this.saving.set(false);
        },
      });
  }
}

