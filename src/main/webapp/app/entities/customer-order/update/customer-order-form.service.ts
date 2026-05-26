import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICustomerOrder, NewCustomerOrder } from '../customer-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomerOrder for edit and NewCustomerOrderFormGroupInput for create.
 */
type CustomerOrderFormGroupInput = ICustomerOrder | PartialWithRequiredKeyOf<NewCustomerOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustomerOrder | NewCustomerOrder> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type CustomerOrderFormRawValue = FormValueOf<ICustomerOrder>;

type NewCustomerOrderFormRawValue = FormValueOf<NewCustomerOrder>;

type CustomerOrderFormDefaults = Pick<NewCustomerOrder, 'id' | 'createdDate'>;

type CustomerOrderFormGroupContent = {
  id: FormControl<CustomerOrderFormRawValue['id'] | NewCustomerOrder['id']>;
  orderNumber: FormControl<CustomerOrderFormRawValue['orderNumber']>;
  status: FormControl<CustomerOrderFormRawValue['status']>;
  totalAmount: FormControl<CustomerOrderFormRawValue['totalAmount']>;
  createdDate: FormControl<CustomerOrderFormRawValue['createdDate']>;
  remarks: FormControl<CustomerOrderFormRawValue['remarks']>;
  customer: FormControl<CustomerOrderFormRawValue['customer']>;
};

export type CustomerOrderFormGroup = FormGroup<CustomerOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerOrderFormService {
  createCustomerOrderFormGroup(customerOrder: CustomerOrderFormGroupInput = { id: null }): CustomerOrderFormGroup {
    const customerOrderRawValue = this.convertCustomerOrderToCustomerOrderRawValue({
      ...this.getFormDefaults(),
      ...customerOrder,
    });
    return new FormGroup<CustomerOrderFormGroupContent>({
      id: new FormControl(
        { value: customerOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      orderNumber: new FormControl(customerOrderRawValue.orderNumber, {
        validators: [Validators.required],
      }),
      status: new FormControl(customerOrderRawValue.status, {
        validators: [Validators.required],
      }),
      totalAmount: new FormControl(customerOrderRawValue.totalAmount, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(customerOrderRawValue.createdDate, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(customerOrderRawValue.remarks),
      customer: new FormControl(customerOrderRawValue.customer),
    });
  }

  getCustomerOrder(form: CustomerOrderFormGroup): ICustomerOrder | NewCustomerOrder {
    return this.convertCustomerOrderRawValueToCustomerOrder(form.getRawValue() as CustomerOrderFormRawValue | NewCustomerOrderFormRawValue);
  }

  resetForm(form: CustomerOrderFormGroup, customerOrder: CustomerOrderFormGroupInput): void {
    const customerOrderRawValue = this.convertCustomerOrderToCustomerOrderRawValue({ ...this.getFormDefaults(), ...customerOrder });
    form.reset(
      {
        ...customerOrderRawValue,
        id: { value: customerOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerOrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertCustomerOrderRawValueToCustomerOrder(
    rawCustomerOrder: CustomerOrderFormRawValue | NewCustomerOrderFormRawValue,
  ): ICustomerOrder | NewCustomerOrder {
    return {
      ...rawCustomerOrder,
      createdDate: dayjs(rawCustomerOrder.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertCustomerOrderToCustomerOrderRawValue(
    customerOrder: ICustomerOrder | (Partial<NewCustomerOrder> & CustomerOrderFormDefaults),
  ): CustomerOrderFormRawValue | PartialWithRequiredKeyOf<NewCustomerOrderFormRawValue> {
    return {
      ...customerOrder,
      createdDate: customerOrder.createdDate ? customerOrder.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
