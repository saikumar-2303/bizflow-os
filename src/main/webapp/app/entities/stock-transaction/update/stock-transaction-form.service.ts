import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStockTransaction, NewStockTransaction } from '../stock-transaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStockTransaction for edit and NewStockTransactionFormGroupInput for create.
 */
type StockTransactionFormGroupInput = IStockTransaction | PartialWithRequiredKeyOf<NewStockTransaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IStockTransaction | NewStockTransaction> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type StockTransactionFormRawValue = FormValueOf<IStockTransaction>;

type NewStockTransactionFormRawValue = FormValueOf<NewStockTransaction>;

type StockTransactionFormDefaults = Pick<NewStockTransaction, 'id' | 'createdDate'>;

type StockTransactionFormGroupContent = {
  id: FormControl<StockTransactionFormRawValue['id'] | NewStockTransaction['id']>;
  transactionType: FormControl<StockTransactionFormRawValue['transactionType']>;
  quantity: FormControl<StockTransactionFormRawValue['quantity']>;
  previousStock: FormControl<StockTransactionFormRawValue['previousStock']>;
  newStock: FormControl<StockTransactionFormRawValue['newStock']>;
  remarks: FormControl<StockTransactionFormRawValue['remarks']>;
  createdDate: FormControl<StockTransactionFormRawValue['createdDate']>;
  product: FormControl<StockTransactionFormRawValue['product']>;
};

export type StockTransactionFormGroup = FormGroup<StockTransactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StockTransactionFormService {
  createStockTransactionFormGroup(stockTransaction: StockTransactionFormGroupInput = { id: null }): StockTransactionFormGroup {
    const stockTransactionRawValue = this.convertStockTransactionToStockTransactionRawValue({
      ...this.getFormDefaults(),
      ...stockTransaction,
    });
    return new FormGroup<StockTransactionFormGroupContent>({
      id: new FormControl(
        { value: stockTransactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      transactionType: new FormControl(stockTransactionRawValue.transactionType, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(stockTransactionRawValue.quantity, {
        validators: [Validators.required],
      }),
      previousStock: new FormControl(stockTransactionRawValue.previousStock, {
        validators: [Validators.required],
      }),
      newStock: new FormControl(stockTransactionRawValue.newStock, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(stockTransactionRawValue.remarks),
      createdDate: new FormControl(stockTransactionRawValue.createdDate, {
        validators: [Validators.required],
      }),
      product: new FormControl(stockTransactionRawValue.product),
    });
  }

  getStockTransaction(form: StockTransactionFormGroup): IStockTransaction | NewStockTransaction {
    return this.convertStockTransactionRawValueToStockTransaction(
      form.getRawValue() as StockTransactionFormRawValue | NewStockTransactionFormRawValue,
    );
  }

  resetForm(form: StockTransactionFormGroup, stockTransaction: StockTransactionFormGroupInput): void {
    const stockTransactionRawValue = this.convertStockTransactionToStockTransactionRawValue({
      ...this.getFormDefaults(),
      ...stockTransaction,
    });
    form.reset(
      {
        ...stockTransactionRawValue,
        id: { value: stockTransactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StockTransactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertStockTransactionRawValueToStockTransaction(
    rawStockTransaction: StockTransactionFormRawValue | NewStockTransactionFormRawValue,
  ): IStockTransaction | NewStockTransaction {
    return {
      ...rawStockTransaction,
      createdDate: dayjs(rawStockTransaction.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertStockTransactionToStockTransactionRawValue(
    stockTransaction: IStockTransaction | (Partial<NewStockTransaction> & StockTransactionFormDefaults),
  ): StockTransactionFormRawValue | PartialWithRequiredKeyOf<NewStockTransactionFormRawValue> {
    return {
      ...stockTransaction,
      createdDate: stockTransaction.createdDate ? stockTransaction.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
