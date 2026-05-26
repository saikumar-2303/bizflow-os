import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';

export interface IStockTransaction {
  id: number;
  transactionType?: string | null;
  quantity?: number | null;
  previousStock?: number | null;
  newStock?: number | null;
  remarks?: string | null;
  createdDate?: dayjs.Dayjs | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewStockTransaction = Omit<IStockTransaction, 'id'> & { id: null };
