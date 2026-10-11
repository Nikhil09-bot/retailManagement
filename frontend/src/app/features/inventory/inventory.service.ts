import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../core/models/api-response.model';
import { InventoryHistory, LowStockProduct, StockAdjustmentRequest } from './inventory.model';
import { Product } from '../products/product.model';

@Injectable({ providedIn: 'root' })
export class InventoryService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/inventory`;

  getLowStock(): Observable<LowStockProduct[]> {
    return this.http
      .get<ApiResponse<LowStockProduct[]>>(`${this.baseUrl}/low-stock`)
      .pipe(map((res) => res.data));
  }

  getOutOfStock(): Observable<Product[]> {
    return this.http
      .get<ApiResponse<Product[]>>(`${this.baseUrl}/out-of-stock`)
      .pipe(map((res) => res.data));
  }

  getHistory(productId: number): Observable<InventoryHistory[]> {
    return this.http
      .get<ApiResponse<InventoryHistory[]>>(`${this.baseUrl}/history/${productId}`)
      .pipe(map((res) => res.data));
  }

  adjustStock(request: StockAdjustmentRequest): Observable<InventoryHistory> {
    return this.http
      .post<ApiResponse<InventoryHistory>>(`${this.baseUrl}/adjust`, request)
      .pipe(map((res) => res.data));
  }
}

