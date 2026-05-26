import dayjs from 'dayjs/esm';

import { ICustomerOrder, NewCustomerOrder } from './customer-order.model';

export const sampleWithRequiredData: ICustomerOrder = {
  id: 2458,
  orderNumber: 'afore gorgeous syringe',
  status: 'muddy cruelly gigantic',
  totalAmount: 23319.87,
  createdDate: dayjs('2026-05-20T07:54'),
};

export const sampleWithPartialData: ICustomerOrder = {
  id: 22678,
  orderNumber: 'upon',
  status: 'but new until',
  totalAmount: 11356.56,
  createdDate: dayjs('2026-05-19T23:19'),
};

export const sampleWithFullData: ICustomerOrder = {
  id: 24520,
  orderNumber: 'continually optimal',
  status: 'revoke',
  totalAmount: 9968.79,
  createdDate: dayjs('2026-05-20T09:34'),
  remarks: 'for boohoo',
};

export const sampleWithNewData: NewCustomerOrder = {
  orderNumber: 'encode thoughtfully',
  status: 'mundane mosh finally',
  totalAmount: 12487.89,
  createdDate: dayjs('2026-05-19T18:16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
