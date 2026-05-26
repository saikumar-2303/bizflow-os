import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpense, NewExpense } from '../expense.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpense for edit and NewExpenseFormGroupInput for create.
 */
type ExpenseFormGroupInput = IExpense | PartialWithRequiredKeyOf<NewExpense>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExpense | NewExpense> = Omit<T, 'expenseDate'> & {
  expenseDate?: string | null;
};

type ExpenseFormRawValue = FormValueOf<IExpense>;

type NewExpenseFormRawValue = FormValueOf<NewExpense>;

type ExpenseFormDefaults = Pick<NewExpense, 'id' | 'expenseDate'>;

type ExpenseFormGroupContent = {
  id: FormControl<ExpenseFormRawValue['id'] | NewExpense['id']>;
  title: FormControl<ExpenseFormRawValue['title']>;
  category: FormControl<ExpenseFormRawValue['category']>;
  amount: FormControl<ExpenseFormRawValue['amount']>;
  expenseDate: FormControl<ExpenseFormRawValue['expenseDate']>;
  remarks: FormControl<ExpenseFormRawValue['remarks']>;
  employee: FormControl<ExpenseFormRawValue['employee']>;
};

export type ExpenseFormGroup = FormGroup<ExpenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseFormService {
  createExpenseFormGroup(expense: ExpenseFormGroupInput = { id: null }): ExpenseFormGroup {
    const expenseRawValue = this.convertExpenseToExpenseRawValue({
      ...this.getFormDefaults(),
      ...expense,
    });
    return new FormGroup<ExpenseFormGroupContent>({
      id: new FormControl(
        { value: expenseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(expenseRawValue.title, {
        validators: [Validators.required],
      }),
      category: new FormControl(expenseRawValue.category),
      amount: new FormControl(expenseRawValue.amount, {
        validators: [Validators.required],
      }),
      expenseDate: new FormControl(expenseRawValue.expenseDate, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(expenseRawValue.remarks),
      employee: new FormControl(expenseRawValue.employee),
    });
  }

  getExpense(form: ExpenseFormGroup): IExpense | NewExpense {
    return this.convertExpenseRawValueToExpense(form.getRawValue() as ExpenseFormRawValue | NewExpenseFormRawValue);
  }

  resetForm(form: ExpenseFormGroup, expense: ExpenseFormGroupInput): void {
    const expenseRawValue = this.convertExpenseToExpenseRawValue({ ...this.getFormDefaults(), ...expense });
    form.reset(
      {
        ...expenseRawValue,
        id: { value: expenseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpenseFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      expenseDate: currentTime,
    };
  }

  private convertExpenseRawValueToExpense(rawExpense: ExpenseFormRawValue | NewExpenseFormRawValue): IExpense | NewExpense {
    return {
      ...rawExpense,
      expenseDate: dayjs(rawExpense.expenseDate, DATE_TIME_FORMAT),
    };
  }

  private convertExpenseToExpenseRawValue(
    expense: IExpense | (Partial<NewExpense> & ExpenseFormDefaults),
  ): ExpenseFormRawValue | PartialWithRequiredKeyOf<NewExpenseFormRawValue> {
    return {
      ...expense,
      expenseDate: expense.expenseDate ? expense.expenseDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
