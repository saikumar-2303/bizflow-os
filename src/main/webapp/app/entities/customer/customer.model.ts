export interface ICustomer {
  id: number;
  name?: string | null;
  phone?: string | null;
  address?: string | null;
  pendingAmount?: number | null;
  active?: boolean | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
