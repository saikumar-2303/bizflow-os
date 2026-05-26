import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'active'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  name: FormControl<IProduct['name']>;
  category: FormControl<IProduct['category']>;
  description: FormControl<IProduct['description']>;
  buyPrice: FormControl<IProduct['buyPrice']>;
  sellPrice: FormControl<IProduct['sellPrice']>;
  stockQuantity: FormControl<IProduct['stockQuantity']>;
  lowStockAlert: FormControl<IProduct['lowStockAlert']>;
  barcode: FormControl<IProduct['barcode']>;
  active: FormControl<IProduct['active']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = {
      ...this.getFormDefaults(),
      ...product,
    };
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productRawValue.name, {
        validators: [Validators.required],
      }),
      category: new FormControl(productRawValue.category),
      description: new FormControl(productRawValue.description),
      buyPrice: new FormControl(productRawValue.buyPrice, {
        validators: [Validators.required],
      }),
      sellPrice: new FormControl(productRawValue.sellPrice, {
        validators: [Validators.required],
      }),
      stockQuantity: new FormControl(productRawValue.stockQuantity, {
        validators: [Validators.required],
      }),
      lowStockAlert: new FormControl(productRawValue.lowStockAlert),
      barcode: new FormControl(productRawValue.barcode),
      active: new FormControl(productRawValue.active, {
        validators: [Validators.required],
      }),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return form.getRawValue() as IProduct | NewProduct;
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = { ...this.getFormDefaults(), ...product };
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
