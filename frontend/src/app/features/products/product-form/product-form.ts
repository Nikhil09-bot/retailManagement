import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-product-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './product-form.html',
  styleUrl: './product-form.scss',
})
export class ProductFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(ProductService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly productId = signal<number | null>(null);
  readonly saving = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    productCode: ['', Validators.required],
    barcode: [''],
    name: ['', Validators.required],
    description: [''],
    brand: [''],
    category: [''],
    purchasePrice: [0, [Validators.required, Validators.min(0)]],
    sellingPrice: [0, [Validators.required, Validators.min(0)]],
    quantityInStock: [0, [Validators.required, Validators.min(0)]],
    reorderLevel: [0, [Validators.required, Validators.min(0)]],
    unit: [''],
  });

  get isEditMode(): boolean {
    return this.productId() !== null;
  }

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.productId.set(id);
      this.service.getById(id).subscribe((product) => {
        this.form.patchValue({
          productCode: product.productCode,
          barcode: product.barcode ?? '',
          name: product.name,
          description: product.description ?? '',
          brand: product.brand ?? '',
          category: product.category ?? '',
          purchasePrice: product.purchasePrice,
          sellingPrice: product.sellingPrice,
          quantityInStock: product.quantityInStock,
          reorderLevel: product.reorderLevel,
          unit: product.unit ?? '',
        });
      });
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.errorMessage.set(null);
    this.saving.set(true);
    const request = this.form.getRawValue();

    const result$ = this.isEditMode
      ? this.service.update(this.productId()!, request)
      : this.service.create(request);

    result$.subscribe({
      next: () => this.router.navigate(['/products']),
      error: () => {
        this.errorMessage.set('Unable to save product. Please check the values and try again.');
        this.saving.set(false);
      },
    });
  }
}

