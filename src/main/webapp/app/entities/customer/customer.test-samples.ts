import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
  name: 'slake splurge',
  active: true,
};

export const sampleWithPartialData: ICustomer = {
  id: 24067,
  name: 'whoa',
  active: true,
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  name: 'where known',
  phone: '779-272-6432',
  address: 'instead',
  pendingAmount: 10695.35,
  active: true,
};

export const sampleWithNewData: NewCustomer = {
  name: 'thyme inasmuch',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
