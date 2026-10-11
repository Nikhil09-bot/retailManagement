import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../core/models/api-response.model';
import { Supplier, SupplierRequest } from './supplier.model';

@Injectable({ providedIn: 'root' })
export class SupplierService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/suppliers`;

  getAll(): Observable<Supplier[]> {
    return this.http.get<ApiResponse<Supplier[]>>(this.baseUrl).pipe(map((res) => res.data));
  }

  getById(id: number): Observable<Supplier> {
    return this.http
      .get<ApiResponse<Supplier>>(`${this.baseUrl}/${id}`)
      .pipe(map((res) => res.data));
  }

  search(keyword: string): Observable<Supplier[]> {
    return this.http
      .get<ApiResponse<Supplier[]>>(`${this.baseUrl}/search`, { params: { keyword } })
      .pipe(map((res) => res.data));
  }

  create(request: SupplierRequest): Observable<Supplier> {
    return this.http.post<ApiResponse<Supplier>>(this.baseUrl, request).pipe(map((res) => res.data));
  }

  update(id: number, request: SupplierRequest): Observable<Supplier> {
    return this.http
      .put<ApiResponse<Supplier>>(`${this.baseUrl}/${id}`, request)
      .pipe(map((res) => res.data));
  }

  deactivate(id: number): Observable<Supplier> {
    return this.http
      .patch<ApiResponse<Supplier>>(`${this.baseUrl}/${id}/deactivate`, {})
      .pipe(map((res) => res.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`).pipe(map(() => undefined));
  }
}

