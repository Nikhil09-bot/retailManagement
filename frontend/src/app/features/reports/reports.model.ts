export interface SalesReport {
  period: string;
  periodLabel: string;
  totalTransactions: number;
  completedTransactions: number;
  cancelledTransactions: number;
  totalRevenue: number;
  totalTax: number;
  totalDiscount: number;
  netRevenue: number;
  averageTransactionValue: number;
}

export interface TopSellingProduct {
  productId: number;
  productCode: string;
  productName: string;
  brand: string | null;
  category: string | null;
  totalQuantitySold: number;
  totalRevenue: number;
}

