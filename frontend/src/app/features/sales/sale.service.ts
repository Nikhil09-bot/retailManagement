import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../core/models/api-response.model';
import { CreateSaleRequest, Sale } from './sale.model';

@Injectable({ providedIn: 'root' })
export class SaleService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/sales`;

  getAll(): Observable<Sale[]> {
    return this.http.get<ApiResponse<Sale[]>>(this.baseUrl).pipe(map((res) => res.data));
  }

  getById(id: number): Observable<Sale> {
    return this.http.get<ApiResponse<Sale>>(`${this.baseUrl}/${id}`).pipe(map((res) => res.data));
  }

  create(request: CreateSaleRequest): Observable<Sale> {
    return this.http.post<ApiResponse<Sale>>(this.baseUrl, request).pipe(map((res) => res.data));
  }

  cancel(id: number): Observable<Sale> {
    return this.http
      .patch<ApiResponse<Sale>>(`${this.baseUrl}/${id}/cancel`, {})
      .pipe(map((res) => res.data));
  }

  downloadInvoicePdf(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}/invoice/pdf`, { responseType: 'blob' });
  }
}

