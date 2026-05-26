import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 8899,
  name: 'blushing institute',
  active: false,
};

export const sampleWithPartialData: IEmployee = {
  id: 16412,
  name: 'cautiously',
  phone: '950-571-2323 x7298',
  designation: 'affiliate whoever consequently',
  salary: 10950.32,
  joiningDate: dayjs('2026-05-19T15:15'),
  active: false,
};

export const sampleWithFullData: IEmployee = {
  id: 19019,
  name: 'sharply questionably softly',
  phone: '916.377.9957 x669',
  designation: 'anti curiously',
  salary: 21920.38,
  joiningDate: dayjs('2026-05-19T22:32'),
  active: false,
};

export const sampleWithNewData: NewEmployee = {
  name: 'unless',
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
