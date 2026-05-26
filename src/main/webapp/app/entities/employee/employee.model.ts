import dayjs from 'dayjs/esm';

export interface IEmployee {
  id: number;
  name?: string | null;
  phone?: string | null;
  designation?: string | null;
  salary?: number | null;
  joiningDate?: dayjs.Dayjs | null;
  active?: boolean | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
