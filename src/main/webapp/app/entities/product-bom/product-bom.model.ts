import { IProduct } from 'app/entities/product/product.model';

export interface IProductBom {
  id: number;
  requiredQuantity?: number | null;
  remarks?: string | null;
  finishedProduct?: Pick<IProduct, 'id' | 'name'> | null;
  rawMaterial?: Pick<IProduct, 'id' | 'name'> | null;
}
export type NewProductBom = Omit<IProductBom, 'id'> & { id: null };
