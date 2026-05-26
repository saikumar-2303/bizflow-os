import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface ICustomerOrder {
  id: number;
  orderNumber?: string | null;
  status?: string | null;
  totalAmount?: number | null;
  createdDate?: dayjs.Dayjs | null;
  remarks?: string | null;
  customer?: Pick<ICustomer, 'id' | 'name'> | null;
}

export type NewCustomerOrder = Omit<ICustomerOrder, 'id'> & { id: null };
