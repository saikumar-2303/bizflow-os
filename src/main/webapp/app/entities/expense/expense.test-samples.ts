import dayjs from 'dayjs/esm';

import { IExpense, NewExpense } from './expense.model';

export const sampleWithRequiredData: IExpense = {
  id: 5007,
  title: 'pfft inasmuch',
  amount: 3209.97,
  expenseDate: dayjs('2026-05-19T20:10'),
};

export const sampleWithPartialData: IExpense = {
  id: 22347,
  title: 'whereas frankly mispronounce',
  amount: 3798.47,
  expenseDate: dayjs('2026-05-20T15:10'),
};

export const sampleWithFullData: IExpense = {
  id: 30901,
  title: 'within notwithstanding where',
  category: 'joyously',
  amount: 28069.26,
  expenseDate: dayjs('2026-05-20T07:21'),
  remarks: 'shovel',
};

export const sampleWithNewData: NewExpense = {
  title: 'versus alongside',
  amount: 20169.7,
  expenseDate: dayjs('2026-05-19T17:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
