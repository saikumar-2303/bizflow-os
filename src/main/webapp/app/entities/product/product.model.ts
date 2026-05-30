export interface IProduct {
  id: number;
  sku?: string | null;
  barcode?: string | null;
  name?: string | null;
  category?: string | null;
  shape?: string | null;
  retailPack?: number | null;
  wholesalePack?: number | null;
  description?: string | null;
  stockQuantity?: number | null;
  lowStockAlert?: number | null;
  remarks?: string | null;
  location?: string | null;
  message?: string | null;
  value?: string | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
