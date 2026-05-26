import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IInvoice {
  id: number;
  invoiceNumber?: string | null;
  totalAmount?: number | null;
  paidAmount?: number | null;
  pendingAmount?: number | null;
  paymentStatus?: string | null;
  createdDate?: dayjs.Dayjs | null;
  remarks?: string | null;
  customer?: Pick<ICustomer, 'id' | 'name'> | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
