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

type ProductFormDefaults = Pick<NewProduct, 'id'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  sku: FormControl<IProduct['sku']>;
  barcode: FormControl<IProduct['barcode']>;
  name: FormControl<IProduct['name']>;
  category: FormControl<IProduct['category']>;
  shape: FormControl<IProduct['shape']>;
  retailPack: FormControl<IProduct['retailPack']>;
  wholesalePack: FormControl<IProduct['wholesalePack']>;
  description: FormControl<IProduct['description']>;
  stockQuantity: FormControl<IProduct['stockQuantity']>;
  lowStockAlert: FormControl<IProduct['lowStockAlert']>;
  remarks: FormControl<IProduct['remarks']>;
  location: FormControl<IProduct['location']>;
  message: FormControl<IProduct['message']>;
  value: FormControl<IProduct['value']>;
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
      sku: new FormControl(productRawValue.sku, {
        validators: [Validators.required],
      }),
      barcode: new FormControl(productRawValue.barcode, {
        validators: [Validators.required],
      }),
      name: new FormControl(productRawValue.name, {
        validators: [Validators.required],
      }),
      category: new FormControl(productRawValue.category),
      shape: new FormControl(productRawValue.shape),
      retailPack: new FormControl(productRawValue.retailPack),
      wholesalePack: new FormControl(productRawValue.wholesalePack),
      description: new FormControl(productRawValue.description),
      stockQuantity: new FormControl(productRawValue.stockQuantity),
      lowStockAlert: new FormControl(productRawValue.lowStockAlert),
      remarks: new FormControl(productRawValue.remarks),
      location: new FormControl(productRawValue.location),
      message: new FormControl(productRawValue.message),
      value: new FormControl(productRawValue.value),
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
    };
  }
}
