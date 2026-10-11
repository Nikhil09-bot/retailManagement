export interface SaleItemRequest {
  productId: number;
  quantity: number;
}

export interface CreateSaleRequest {
  items: SaleItemRequest[];
  taxRate: number;
  discountAmount: number;
  paymentMethod: string;
  notes?: string | null;
}

export interface SaleItemResponse {
  id: number;
  productId: number;
  productCode: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
}

export interface Sale {
  id: number;
  receiptNumber: string;
  status: string;
  subtotal: number;
  taxAmount: number;
  discountAmount: number;
  grandTotal: number;
  paymentMethod: string;
  notes: string | null;
  items: SaleItemResponse[];
  createdAt: string;
}

