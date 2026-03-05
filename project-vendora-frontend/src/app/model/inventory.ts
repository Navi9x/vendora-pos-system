export interface CategoryDTO {
  id: number;
  name: string;
}

export type UnitType = 'PIECE' | 'KG' | 'GRAM' | 'LITER' | 'ML';

export interface ProductSummaryDTO {
  id: number;
  name: string;
  model?: string | null;
  sku?: string | null;
  barcode?: string | null;

  price: number;
  costPrice?: number | null;
  image?: string | null;

  unitType?: UnitType | null;
  isTaxable?: boolean | null;

  genericName?: string | null;
  manufacturer?: string | null;
  expiryDate?: string | null;   // backend sends as "YYYY-MM-DD" or null
  batchNumber?: string | null;
  requiresPrescription?: boolean | null;
  isActive?: boolean | null;

  category?: CategoryDTO | null;
}

export interface InventoryResponseDTO {
  id: number;
  createdAt: string;
  updatedAt: string;
  isDeleted: boolean;

  businessId: number;

  quantity: number;            // backend is Double
  minStockLevel?: number | null;
  maxStockLevel?: number | null;
  lastRestocked?: string | null;

  product: ProductSummaryDTO;
}
