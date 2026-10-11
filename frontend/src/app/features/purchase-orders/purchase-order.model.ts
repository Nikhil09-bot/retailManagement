export interface PurchaseOrderItemRequest {
  productId: number;
  quantityOrdered: number;
  unitPrice: number;
}

export interface CreatePurchaseOrderRequest {
  supplierId: number;
  items: PurchaseOrderItemRequest[];
  expectedDeliveryDate?: string | null;
  notes?: string | null;
}

export interface PurchaseOrderItemResponse {
  id: number;
  productId: number;
  productCode: string;
  productName: string;
  quantityOrdered: number;
  quantityReceived: number;
  unitPrice: number;
  lineTotal: number;
}

export interface PurchaseOrder {
  id: number;
  poNumber: string;
  supplierId: number;
  supplierName: string;
  status: string;
  totalAmount: number;
  receivedAmount: number;
  expectedDeliveryDate: string | null;
  receivedDate: string | null;
  notes: string | null;
  items: PurchaseOrderItemResponse[];
  createdAt: string;
  updatedAt: string;
}

