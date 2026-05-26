import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';
import { CustomerOrderService } from 'app/entities/customer-order/service/customer-order.service';
import { OrderItemService } from '../service/order-item.service';
import { IOrderItem } from '../order-item.model';
import { OrderItemFormGroup, OrderItemFormService } from './order-item-form.service';

@Component({
  selector: 'jhi-order-item-update',
  templateUrl: './order-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrderItemUpdateComponent implements OnInit {
  isSaving = false;
  orderItem: IOrderItem | null = null;

  productsSharedCollection: IProduct[] = [];
  customerOrdersSharedCollection: ICustomerOrder[] = [];

  protected orderItemService = inject(OrderItemService);
  protected orderItemFormService = inject(OrderItemFormService);
  protected productService = inject(ProductService);
  protected customerOrderService = inject(CustomerOrderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderItemFormGroup = this.orderItemFormService.createOrderItemFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareCustomerOrder = (o1: ICustomerOrder | null, o2: ICustomerOrder | null): boolean =>
    this.customerOrderService.compareCustomerOrder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItem }) => {
      this.orderItem = orderItem;
      if (orderItem) {
        this.updateForm(orderItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderItem = this.orderItemFormService.getOrderItem(this.editForm);
    if (orderItem.id !== null) {
      this.subscribeToSaveResponse(this.orderItemService.update(orderItem));
    } else {
      this.subscribeToSaveResponse(this.orderItemService.create(orderItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItem>>): void {
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

  protected updateForm(orderItem: IOrderItem): void {
    this.orderItem = orderItem;
    this.orderItemFormService.resetForm(this.editForm, orderItem);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      orderItem.product,
    );
    this.customerOrdersSharedCollection = this.customerOrderService.addCustomerOrderToCollectionIfMissing<ICustomerOrder>(
      this.customerOrdersSharedCollection,
      orderItem.order,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.orderItem?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.customerOrderService
      .query()
      .pipe(map((res: HttpResponse<ICustomerOrder[]>) => res.body ?? []))
      .pipe(
        map((customerOrders: ICustomerOrder[]) =>
          this.customerOrderService.addCustomerOrderToCollectionIfMissing<ICustomerOrder>(customerOrders, this.orderItem?.order),
        ),
      )
      .subscribe((customerOrders: ICustomerOrder[]) => (this.customerOrdersSharedCollection = customerOrders));
  }
}
