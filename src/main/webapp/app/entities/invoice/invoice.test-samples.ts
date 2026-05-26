import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 29050,
  invoiceNumber: 'search why',
  totalAmount: 31544.08,
  paidAmount: 2181.18,
  pendingAmount: 28649.55,
  paymentStatus: 'but dependency why',
  createdDate: dayjs('2026-05-20T05:30'),
};

export const sampleWithPartialData: IInvoice = {
  id: 9408,
  invoiceNumber: 'lawmaker verve',
  totalAmount: 5518.54,
  paidAmount: 4844.6,
  pendingAmount: 30888.23,
  paymentStatus: 'sneaky',
  createdDate: dayjs('2026-05-19T21:41'),
};

export const sampleWithFullData: IInvoice = {
  id: 19628,
  invoiceNumber: 'yesterday times',
  totalAmount: 10826.99,
  paidAmount: 6593.35,
  pendingAmount: 11188.03,
  paymentStatus: 'upwardly',
  createdDate: dayjs('2026-05-20T06:20'),
  remarks: 'knotty fill throughout',
};

export const sampleWithNewData: NewInvoice = {
  invoiceNumber: 'within about',
  totalAmount: 15726.22,
  paidAmount: 5947.02,
  pendingAmount: 5763.64,
  paymentStatus: 'mild across',
  createdDate: dayjs('2026-05-20T03:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
