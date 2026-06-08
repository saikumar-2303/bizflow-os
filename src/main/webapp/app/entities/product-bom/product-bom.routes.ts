import { Routes } from '@angular/router';
import { ProductBomComponent } from './product-bom.component';

export const productBomRoute: Routes = [
  {
    path: '', // Leave empty because the parent router will define the main prefix
    component: ProductBomComponent,
    data: { pageTitle: 'Create Product BOM' },
  },
];

export default productBomRoute;
