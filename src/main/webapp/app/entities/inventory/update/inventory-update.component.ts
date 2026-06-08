import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IInventory } from '../inventory.model';
import { InventoryService } from '../service/inventory.service';
import { InventoryFormGroup, InventoryFormService } from './inventory-form.service';

@Component({
  selector: 'jhi-inventory-update',
  templateUrl: './inventory-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InventoryUpdateComponent implements OnInit {
  isSaving = false;
  inventory: IInventory | null = null;

  productsSharedCollection: IProduct[] = [];

  protected inventoryService = inject(InventoryService);
  protected inventoryFormService = inject(InventoryFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryFormGroup = this.inventoryFormService.createInventoryFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventory }) => {
      this.inventory = inventory;
      if (inventory) {
        this.updateForm(inventory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  // save(): void {
  //   this.isSaving = true;
  //   const inventory = this.inventoryFormService.getInventory(this.editForm);
  //   if (inventory.id !== null) {
  //     this.subscribeToSaveResponse(this.inventoryService.update(inventory));
  //   } else {
  //     this.subscribeToSaveResponse(this.inventoryService.create(inventory));
  //   }
  // }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(inventory: IInventory): void {
    this.inventory = inventory;
    this.inventoryFormService.resetForm(this.editForm, inventory);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      inventory.product_id,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.inventory?.product_id)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
