export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
  phone: string;
  email: string | null;
  address: string | null;
  city: string | null;
  loyaltyPoints: number;
  totalPurchases: number;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CustomerRequest {
  firstName: string;
  lastName: string;
  phone: string;
  email?: string | null;
  address?: string | null;
  city?: string | null;
  notes?: string | null;
}

