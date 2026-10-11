export interface LowStockProduct {
  productId: number;
  productCode: string;
  productName: string;
  brand: string | null;
  category: string | null;
  quantityInStock: number;
  reorderLevel: number;
  quantityToReorder: number;
}

export interface InventoryHistory {
  id: number;
  productId: number;
  productCode: string;
  productName: string;
  transactionType: string;
  quantityChanged: number;
  quantityBefore: number;
  quantityAfter: number;
  reference: string | null;
  notes: string | null;
  createdAt: string;
}

export interface StockAdjustmentRequest {
  productId: number;
  adjustmentQuantity: number;
  reason: string;
  notes?: string | null;
}

