export interface IProduct {
  id: number;
  name?: string | null;
  category?: string | null;
  description?: string | null;
  buyPrice?: number | null;
  sellPrice?: number | null;
  stockQuantity?: number | null;
  lowStockAlert?: number | null;
  barcode?: string | null;
  active?: boolean | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
