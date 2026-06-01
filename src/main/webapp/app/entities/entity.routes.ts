import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'bizFlowApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'bizFlowApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'bizFlowApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'customer-order',
    data: { pageTitle: 'bizFlowApp.customerOrder.home.title' },
    loadChildren: () => import('./customer-order/customer-order.routes'),
  },
  {
    path: 'order-item',
    data: { pageTitle: 'bizFlowApp.orderItem.home.title' },
    loadChildren: () => import('./order-item/order-item.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'bizFlowApp.invoice.home.title' },
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  {
    path: 'invoice-item',
    data: { pageTitle: 'bizFlowApp.invoiceItem.home.title' },
    loadChildren: () => import('./invoice-item/invoice-item.routes'),
  },
  {
    path: 'stock-transaction',
    data: { pageTitle: 'bizFlowApp.stockTransaction.home.title' },
    loadChildren: () => import('./stock-transaction/stock-transaction.routes'),
  },
  {
    path: 'expense',
    data: { pageTitle: 'bizFlowApp.expense.home.title' },
    loadChildren: () => import('./expense/expense.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'bizFlowApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'inventory',
    data: { pageTitle: 'bizFlowApp.inventory.home.title' },
    loadChildren: () => import('./inventory/inventory.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
