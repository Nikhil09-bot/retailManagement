import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-customer-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './customer-form.html',
  styleUrl: './customer-form.scss',
})
export class CustomerFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(CustomerService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly customerId = signal<number | null>(null);
  readonly saving = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    phone: ['', Validators.required],
    email: [''],
    address: [''],
    city: [''],
    notes: [''],
  });

  get isEditMode(): boolean {
    return this.customerId() !== null;
  }

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.customerId.set(id);
      this.service.getById(id).subscribe((customer) => {
        this.form.patchValue({
          firstName: customer.firstName,
          lastName: customer.lastName,
          phone: customer.phone,
          email: customer.email ?? '',
          address: customer.address ?? '',
          city: customer.city ?? '',
          notes: customer.notes ?? '',
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
      ? this.service.update(this.customerId()!, request)
      : this.service.create(request);

    result$.subscribe({
      next: () => this.router.navigate(['/customers']),
      error: () => {
        this.errorMessage.set('Unable to save customer. Please check the values and try again.');
        this.saving.set(false);
      },
    });
  }
}

