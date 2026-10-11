import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { SupplierService } from '../supplier.service';

@Component({
  selector: 'app-supplier-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './supplier-form.html',
  styleUrl: './supplier-form.scss',
})
export class SupplierFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(SupplierService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly supplierId = signal<number | null>(null);
  readonly saving = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    supplierCode: ['', Validators.required],
    name: ['', Validators.required],
    contactPerson: [''],
    phone: ['', Validators.required],
    email: [''],
    address: [''],
    city: [''],
    state: [''],
    zipcode: [''],
    country: [''],
    paymentTerms: [''],
    taxId: [''],
    notes: [''],
  });

  get isEditMode(): boolean {
    return this.supplierId() !== null;
  }

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.supplierId.set(id);
      this.service.getById(id).subscribe((supplier) => {
        this.form.patchValue({
          supplierCode: supplier.supplierCode,
          name: supplier.name,
          contactPerson: supplier.contactPerson ?? '',
          phone: supplier.phone,
          email: supplier.email ?? '',
          address: supplier.address ?? '',
          city: supplier.city ?? '',
          state: supplier.state ?? '',
          zipcode: supplier.zipcode ?? '',
          country: supplier.country ?? '',
          paymentTerms: supplier.paymentTerms ?? '',
          taxId: supplier.taxId ?? '',
          notes: supplier.notes ?? '',
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
      ? this.service.update(this.supplierId()!, request)
      : this.service.create(request);

    result$.subscribe({
      next: () => this.router.navigate(['/suppliers']),
      error: () => {
        this.errorMessage.set('Unable to save supplier. Please check the values and try again.');
        this.saving.set(false);
      },
    });
  }
}

