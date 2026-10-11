import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../core/models/api-response.model';
import { CreatePurchaseOrderRequest, PurchaseOrder } from './purchase-order.model';

@Injectable({ providedIn: 'root' })
export class PurchaseOrderService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/purchase-orders`;

  getAll(): Observable<PurchaseOrder[]> {
    return this.http.get<ApiResponse<PurchaseOrder[]>>(this.baseUrl).pipe(map((res) => res.data));
  }

  getById(id: number): Observable<PurchaseOrder> {
    return this.http
      .get<ApiResponse<PurchaseOrder>>(`${this.baseUrl}/${id}`)
      .pipe(map((res) => res.data));
  }

  create(request: CreatePurchaseOrderRequest): Observable<PurchaseOrder> {
    return this.http
      .post<ApiResponse<PurchaseOrder>>(this.baseUrl, request)
      .pipe(map((res) => res.data));
  }

  receive(id: number): Observable<PurchaseOrder> {
    return this.http
      .patch<ApiResponse<PurchaseOrder>>(`${this.baseUrl}/${id}/receive`, {})
      .pipe(map((res) => res.data));
  }

  cancel(id: number): Observable<PurchaseOrder> {
    return this.http
      .patch<ApiResponse<PurchaseOrder>>(`${this.baseUrl}/${id}/cancel`, {})
      .pipe(map((res) => res.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`).pipe(map(() => undefined));
  }
}

