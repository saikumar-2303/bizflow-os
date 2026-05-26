import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInvoiceItem, NewInvoiceItem } from '../invoice-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceItem for edit and NewInvoiceItemFormGroupInput for create.
 */
type InvoiceItemFormGroupInput = IInvoiceItem | PartialWithRequiredKeyOf<NewInvoiceItem>;

type InvoiceItemFormDefaults = Pick<NewInvoiceItem, 'id'>;

type InvoiceItemFormGroupContent = {
  id: FormControl<IInvoiceItem['id'] | NewInvoiceItem['id']>;
  quantity: FormControl<IInvoiceItem['quantity']>;
  price: FormControl<IInvoiceItem['price']>;
  totalAmount: FormControl<IInvoiceItem['totalAmount']>;
  product: FormControl<IInvoiceItem['product']>;
  invoice: FormControl<IInvoiceItem['invoice']>;
};

export type InvoiceItemFormGroup = FormGroup<InvoiceItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceItemFormService {
  createInvoiceItemFormGroup(invoiceItem: InvoiceItemFormGroupInput = { id: null }): InvoiceItemFormGroup {
    const invoiceItemRawValue = {
      ...this.getFormDefaults(),
      ...invoiceItem,
    };
    return new FormGroup<InvoiceItemFormGroupContent>({
      id: new FormControl(
        { value: invoiceItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantity: new FormControl(invoiceItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      price: new FormControl(invoiceItemRawValue.price, {
        validators: [Validators.required],
      }),
      totalAmount: new FormControl(invoiceItemRawValue.totalAmount, {
        validators: [Validators.required],
      }),
      product: new FormControl(invoiceItemRawValue.product),
      invoice: new FormControl(invoiceItemRawValue.invoice),
    });
  }

  getInvoiceItem(form: InvoiceItemFormGroup): IInvoiceItem | NewInvoiceItem {
    return form.getRawValue() as IInvoiceItem | NewInvoiceItem;
  }

  resetForm(form: InvoiceItemFormGroup, invoiceItem: InvoiceItemFormGroupInput): void {
    const invoiceItemRawValue = { ...this.getFormDefaults(), ...invoiceItem };
    form.reset(
      {
        ...invoiceItemRawValue,
        id: { value: invoiceItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceItemFormDefaults {
    return {
      id: null,
    };
  }
}
