import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id' | 'active'>;

type CustomerFormGroupContent = {
  id: FormControl<ICustomer['id'] | NewCustomer['id']>;
  name: FormControl<ICustomer['name']>;
  phone: FormControl<ICustomer['phone']>;
  address: FormControl<ICustomer['address']>;
  pendingAmount: FormControl<ICustomer['pendingAmount']>;
  active: FormControl<ICustomer['active']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = {
      ...this.getFormDefaults(),
      ...customer,
    };
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(customerRawValue.name, {
        validators: [Validators.required],
      }),
      phone: new FormControl(customerRawValue.phone),
      address: new FormControl(customerRawValue.address),
      pendingAmount: new FormControl(customerRawValue.pendingAmount),
      active: new FormControl(customerRawValue.active, {
        validators: [Validators.required],
      }),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return form.getRawValue() as ICustomer | NewCustomer;
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = { ...this.getFormDefaults(), ...customer };
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
