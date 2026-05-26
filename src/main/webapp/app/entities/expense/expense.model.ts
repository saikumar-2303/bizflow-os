import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IExpense {
  id: number;
  title?: string | null;
  category?: string | null;
  amount?: number | null;
  expenseDate?: dayjs.Dayjs | null;
  remarks?: string | null;
  employee?: Pick<IEmployee, 'id' | 'name'> | null;
}

export type NewExpense = Omit<IExpense, 'id'> & { id: null };
