import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../../core/models/api-response.model';
import { Product, ProductRequest } from './product.model';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/products`;

  getAll(): Observable<Product[]> {
    return this.http.get<ApiResponse<Product[]>>(this.baseUrl).pipe(map((res) => res.data));
  }

  getById(id: number): Observable<Product> {
    return this.http.get<ApiResponse<Product>>(`${this.baseUrl}/${id}`).pipe(map((res) => res.data));
  }

  search(keyword: string): Observable<Product[]> {
    return this.http
      .get<ApiResponse<Product[]>>(`${this.baseUrl}/search`, { params: { keyword } })
      .pipe(map((res) => res.data));
  }

  create(request: ProductRequest): Observable<Product> {
    return this.http.post<ApiResponse<Product>>(this.baseUrl, request).pipe(map((res) => res.data));
  }

  update(id: number, request: ProductRequest): Observable<Product> {
    return this.http
      .put<ApiResponse<Product>>(`${this.baseUrl}/${id}`, request)
      .pipe(map((res) => res.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`).pipe(map(() => undefined));
  }

  disable(id: number): Observable<Product> {
    return this.http
      .patch<ApiResponse<Product>>(`${this.baseUrl}/${id}/disable`, {})
      .pipe(map((res) => res.data));
  }

  addStock(id: number, quantity: number): Observable<Product> {
    return this.http
      .patch<ApiResponse<Product>>(`${this.baseUrl}/${id}/stock/add`, null, { params: { quantity } })
      .pipe(map((res) => res.data));
  }

  removeStock(id: number, quantity: number): Observable<Product> {
    return this.http
      .patch<ApiResponse<Product>>(`${this.baseUrl}/${id}/stock/remove`, null, { params: { quantity } })
      .pipe(map((res) => res.data));
  }
}

