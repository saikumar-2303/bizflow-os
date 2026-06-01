import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInventory, NewInventory } from '../inventory.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventory for edit and NewInventoryFormGroupInput for create.
 */
type InventoryFormGroupInput = IInventory | PartialWithRequiredKeyOf<NewInventory>;

type InventoryFormDefaults = Pick<NewInventory, 'id'>;

type InventoryFormGroupContent = {
  id: FormControl<IInventory['id'] | NewInventory['id']>;
  inventory_id: FormControl<IInventory['inventory_id']>;
  remarks: FormControl<IInventory['remarks']>;
  location: FormControl<IInventory['location']>;
  description: FormControl<IInventory['description']>;
  product_id: FormControl<IInventory['product_id']>;
};

export type InventoryFormGroup = FormGroup<InventoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InventoryFormService {
  createInventoryFormGroup(inventory: InventoryFormGroupInput = { id: null }): InventoryFormGroup {
    const inventoryRawValue = {
      ...this.getFormDefaults(),
      ...inventory,
    };
    return new FormGroup<InventoryFormGroupContent>({
      id: new FormControl(
        { value: inventoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      inventory_id: new FormControl(inventoryRawValue.inventory_id, {
        validators: [Validators.required],
      }),
      remarks: new FormControl(inventoryRawValue.remarks),
      location: new FormControl(inventoryRawValue.location),
      description: new FormControl(inventoryRawValue.description),
      product_id: new FormControl(inventoryRawValue.product_id, {
        validators: [Validators.required],
      }),
    });
  }

  getInventory(form: InventoryFormGroup): IInventory | NewInventory {
    return form.getRawValue() as IInventory | NewInventory;
  }

  resetForm(form: InventoryFormGroup, inventory: InventoryFormGroupInput): void {
    const inventoryRawValue = { ...this.getFormDefaults(), ...inventory };
    form.reset(
      {
        ...inventoryRawValue,
        id: { value: inventoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InventoryFormDefaults {
    return {
      id: null,
    };
  }
}
