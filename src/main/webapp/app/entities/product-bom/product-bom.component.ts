import { Component, inject, OnInit } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { ProductBomService } from './product-bom.service';
import { ProductService } from '../product/service/product.service';
import { ActivatedRoute } from '@angular/router';
import { IProductBom } from './product-bom.model';
import { IProduct } from '../product/product.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-product-bom',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './product-bom.component.html',
  styleUrl: './product-bom.component.scss',
})
export class ProductBomComponent implements OnInit {
  isSaving = false;

  // Global BOM properties (Global requiredQuantity has been removed since it is now per item)
  productBom: any = {
    id: null,
    finishedProduct: null,
    remarks: '',
  };

  mainProducts: IProduct[] = [];
  dependentItems: any[] = [];

  // 🎯 FIXED: Initialized as an object containing both its dropdown selection and its custom row quantity
  selectedMaterialsList: any[] = [{ product: null, quantity: 1 }];

  protected productBomService = inject(ProductBomService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    this.loadMainProducts();
  }

  loadMainProducts(): void {
    this.productService.query({ size: 1000 }).subscribe({
      next: (res: HttpResponse<IProduct[]>) => {
        this.mainProducts = res.body ?? [];
      },
      error: err => console.error('Failed to load primary products list:', err),
    });
  }

  /**
   * DEPENDENT DROPDOWN TRACKER
   */
  onProductChange(selectedProduct: IProduct | null): void {
    this.dependentItems = [];
    // Reset back to a single initial structured row entry tracking object
    this.selectedMaterialsList = [{ product: null, quantity: 1 }];

    const fullProductDetails = this.mainProducts.find(p => p.id === selectedProduct?.id);
    const extractedId = fullProductDetails?.id;

    if (extractedId) {
      this.productService.queryDependentItems(extractedId).subscribe({
        next: (res: HttpResponse<any[]>) => {
          this.dependentItems = res.body ?? [];
        },
        error: err => console.error('Error fetching dependent tiered components:', err),
      });
    }
  }

  /**
   * ➕ BUTTON ACTION: Appends a blank structured row layout onto the view array list
   */
  addNewMaterialRow(): void {
    this.selectedMaterialsList.push({ product: null, quantity: 1 });
  }

  /**
   * ❌ BUTTON ACTION: Slices away a targeted entry index row from the selection group
   */
  removeMaterialRow(index: number): void {
    if (this.selectedMaterialsList.length > 1) {
      this.selectedMaterialsList.splice(index, 1);
    }
  }

  /**
   * HELPER METHOD FOR BETTER DROPDOWN DOM TRACKING
   */
  compareProduct(o1: any, o2: any): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  /**
   * SAVING DATA SAFELY
   * 🚀 FIXED: Assembles data to match component.get("rawMaterialId") and component.get("quantity") keys!
   */
  save(): void {
    this.isSaving = true;

    // Filter out rows that were left unselected, then map to your exact backend key structure
    const validItemsCollection = this.selectedMaterialsList
      .filter(row => row.product !== null)
      .map(row => ({
        rawMaterialId: row.product.id, // 🎯 Matches backend: component.get("rawMaterialId")
        name: row.product.name,
        quantity: row.quantity, // 🎯 Matches backend: component.get("quantity")
      }));

    const payload = {
      ...this.productBom,
      id: null,
      items: validItemsCollection,
    };

    // eslint-disable-next-line no-console
    console.log('Sending structured multi-row collection payload with item quantities:', payload);
    this.subscribeToSaveResponse(this.productBomService.create(payload));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductBom>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.previousState(),
      error: () => (this.isSaving = false),
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
