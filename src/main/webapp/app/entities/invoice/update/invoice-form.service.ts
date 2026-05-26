import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'createdDate'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  invoiceNumber: FormControl<InvoiceFormRawValue['invoiceNumber']>;
  totalAmount: FormControl<InvoiceFormRawValue['totalAmount']>;
  paidAmount: FormControl<InvoiceFormRawValue['paidAmount']>;
  pendingAmount: FormControl<InvoiceFormRawValue['pendingAmount']>;
  paymentStatus: FormControl<InvoiceFormRawValue['paymentStatus']>;
  createdDate: FormControl<InvoiceFormRawValue['createdDate']>;
  remarks: FormControl<InvoiceFormRawValue['remarks']>;
  customer: FormControl<InvoiceFormRawValue['customer']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoiceNumber: new FormControl(invoiceRawValue.invoiceNumber, {
        validators: [Validators.required],
      }),
      totalAmount: new FormControl(invoiceRawValue.totalAmount, {
        validators: [Validators.required],
      }),
      paidAmount: new FormControl(invoiceRawValue.paidAmount, {
        validators: [Validators.required],
      }),
      pendingAmount: new FormControl(invoiceRawValue.pendingAmount, {
        validators: [Validators.required],
      }),
      paymentStatus: new FormControl(invoiceRawValue.paymentStatus, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(invoiceRawValue.createdDate, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(invoiceRawValue.remarks),
      customer: new FormControl(invoiceRawValue.customer),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      createdDate: dayjs(rawInvoice.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults),
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      createdDate: invoice.createdDate ? invoice.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
