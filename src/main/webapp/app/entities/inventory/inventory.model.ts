import { IProduct } from 'app/entities/product/product.model';

export interface IInventory {
  id: number;
  inventory_id?: string | null;
  remarks?: string | null;
  location?: string | null;
  description?: string | null;
  product_id?: Pick<IProduct, 'id' | 'sku'> | null;
}

export type NewInventory = Omit<IInventory, 'id'> & { id: null };
