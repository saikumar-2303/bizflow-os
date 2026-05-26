import dayjs from 'dayjs/esm';

import { IStockTransaction, NewStockTransaction } from './stock-transaction.model';

export const sampleWithRequiredData: IStockTransaction = {
  id: 10656,
  transactionType: 'possible judgementally',
  quantity: 4888,
  previousStock: 28715,
  newStock: 23254,
  createdDate: dayjs('2026-05-20T07:18'),
};

export const sampleWithPartialData: IStockTransaction = {
  id: 22442,
  transactionType: 'courageously',
  quantity: 27423,
  previousStock: 23821,
  newStock: 29918,
  remarks: 'if',
  createdDate: dayjs('2026-05-19T16:41'),
};

export const sampleWithFullData: IStockTransaction = {
  id: 6321,
  transactionType: 'perky',
  quantity: 277,
  previousStock: 12291,
  newStock: 24204,
  remarks: 'abaft',
  createdDate: dayjs('2026-05-20T05:52'),
};

export const sampleWithNewData: NewStockTransaction = {
  transactionType: 'excluding gee er',
  quantity: 19165,
  previousStock: 19290,
  newStock: 20727,
  createdDate: dayjs('2026-05-20T00:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
