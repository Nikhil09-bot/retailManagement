import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../core/models/api-response.model';
import { Customer, CustomerRequest } from './customer.model';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/customers`;

  getAll(): Observable<Customer[]> {
    return this.http.get<ApiResponse<Customer[]>>(this.baseUrl).pipe(map((res) => res.data));
  }

  getById(id: number): Observable<Customer> {
    return this.http
      .get<ApiResponse<Customer>>(`${this.baseUrl}/${id}`)
      .pipe(map((res) => res.data));
  }

  search(keyword: string): Observable<Customer[]> {
    return this.http
      .get<ApiResponse<Customer[]>>(`${this.baseUrl}/search`, { params: { keyword } })
      .pipe(map((res) => res.data));
  }

  create(request: CustomerRequest): Observable<Customer> {
    return this.http.post<ApiResponse<Customer>>(this.baseUrl, request).pipe(map((res) => res.data));
  }

  update(id: number, request: CustomerRequest): Observable<Customer> {
    return this.http
      .put<ApiResponse<Customer>>(`${this.baseUrl}/${id}`, request)
      .pipe(map((res) => res.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`).pipe(map(() => undefined));
  }

  addLoyaltyPoints(id: number, points: number): Observable<Customer> {
    return this.http
      .patch<ApiResponse<Customer>>(`${this.baseUrl}/${id}/loyalty-points`, null, { params: { points } })
      .pipe(map((res) => res.data));
  }
}

