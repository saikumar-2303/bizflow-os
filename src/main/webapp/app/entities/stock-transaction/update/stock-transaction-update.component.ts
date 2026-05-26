import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IStockTransaction } from '../stock-transaction.model';
import { StockTransactionService } from '../service/stock-transaction.service';
import { StockTransactionFormGroup, StockTransactionFormService } from './stock-transaction-form.service';

@Component({
  selector: 'jhi-stock-transaction-update',
  templateUrl: './stock-transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StockTransactionUpdateComponent implements OnInit {
  isSaving = false;
  stockTransaction: IStockTransaction | null = null;

  productsSharedCollection: IProduct[] = [];

  protected stockTransactionService = inject(StockTransactionService);
  protected stockTransactionFormService = inject(StockTransactionFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StockTransactionFormGroup = this.stockTransactionFormService.createStockTransactionFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockTransaction }) => {
      this.stockTransaction = stockTransaction;
      if (stockTransaction) {
        this.updateForm(stockTransaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stockTransaction = this.stockTransactionFormService.getStockTransaction(this.editForm);
    if (stockTransaction.id !== null) {
      this.subscribeToSaveResponse(this.stockTransactionService.update(stockTransaction));
    } else {
      this.subscribeToSaveResponse(this.stockTransactionService.create(stockTransaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockTransaction>>): void {
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

  protected updateForm(stockTransaction: IStockTransaction): void {
    this.stockTransaction = stockTransaction;
    this.stockTransactionFormService.resetForm(this.editForm, stockTransaction);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      stockTransaction.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.stockTransaction?.product),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
