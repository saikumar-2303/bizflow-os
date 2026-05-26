import { IProduct } from 'app/entities/product/product.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface IInvoiceItem {
  id: number;
  quantity?: number | null;
  price?: number | null;
  totalAmount?: number | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
  invoice?: Pick<IInvoice, 'id' | 'invoiceNumber'> | null;
}

export type NewInvoiceItem = Omit<IInvoiceItem, 'id'> & { id: null };
