import { IProduct } from 'app/entities/product/product.model';
import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';

export interface IOrderItem {
  id: number;
  quantity?: number | null;
  price?: number | null;
  totalAmount?: number | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
  order?: Pick<ICustomerOrder, 'id' | 'orderNumber'> | null;
}

export type NewOrderItem = Omit<IOrderItem, 'id'> & { id: null };
