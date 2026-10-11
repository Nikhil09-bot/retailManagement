export interface Product {
  id: number;
  productCode: string;
  barcode: string | null;
  name: string;
  description: string | null;
  brand: string | null;
  category: string | null;
  purchasePrice: number;
  sellingPrice: number;
  quantityInStock: number;
  reorderLevel: number;
  unit: string | null;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ProductRequest {
  productCode: string;
  barcode?: string | null;
  name: string;
  description?: string | null;
  brand?: string | null;
  category?: string | null;
  purchasePrice: number;
  sellingPrice: number;
  quantityInStock: number;
  reorderLevel: number;
  unit?: string | null;
}

