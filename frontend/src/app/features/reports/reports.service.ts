import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../core/models/api-response.model';
import { SalesReport, TopSellingProduct } from './reports.model';

@Injectable({ providedIn: 'root' })
export class ReportsService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/reports`;

  getMonthlySales(month: number, year: number): Observable<SalesReport> {
    return this.http
      .get<ApiResponse<SalesReport>>(`${this.baseUrl}/sales/monthly`, { params: { month, year } })
      .pipe(map((res) => res.data));
  }

  getTopProducts(start: string, end: string, limit = 5): Observable<TopSellingProduct[]> {
    return this.http
      .get<ApiResponse<TopSellingProduct[]>>(`${this.baseUrl}/top-products`, {
        params: { start, end, limit },
      })
      .pipe(map((res) => res.data));
  }
}

