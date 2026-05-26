import { IInvoiceItem, NewInvoiceItem } from './invoice-item.model';

export const sampleWithRequiredData: IInvoiceItem = {
  id: 29900,
  quantity: 3310,
  price: 10594.65,
  totalAmount: 31783.2,
};

export const sampleWithPartialData: IInvoiceItem = {
  id: 18164,
  quantity: 10459,
  price: 6529.41,
  totalAmount: 1634.11,
};

export const sampleWithFullData: IInvoiceItem = {
  id: 20248,
  quantity: 27032,
  price: 4018.18,
  totalAmount: 7972.04,
};

export const sampleWithNewData: NewInvoiceItem = {
  quantity: 31747,
  price: 19201.99,
  totalAmount: 11632.24,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
