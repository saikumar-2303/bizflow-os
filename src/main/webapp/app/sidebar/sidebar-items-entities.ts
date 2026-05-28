import SidebarItem from './sidebar-item.model';

export const EntitySidebarItems: SidebarItem[] = [
  {
    name: 'DashBoard',
    route: '/dashboard',
    translationKey: 'global.menu.dashboard',
    icon: 'bi bi-clipboard-data',
  },
  {
    name: 'Product',
    route: '/product',
    translationKey: 'global.menu.entities.product',
    icon: 'bi bi-bag',
  },
  {
    name: 'Customer',
    route: '/customer',
    translationKey: 'global.menu.entities.customer',
    icon: 'bi bi-person',
  },
  {
    name: 'CustomerOrder',
    route: '/customer-order',
    translationKey: 'global.menu.entities.customerOrder',
    icon: 'bi bi-cart',
  },
  {
    name: 'OrderItem',
    route: '/order-item',
    translationKey: 'global.menu.entities.orderItem',
    icon: 'bi bi-box-seam',
  },
  {
    name: 'Invoice',
    route: '/invoice',
    translationKey: 'global.menu.entities.invoice',
    icon: 'bi bi-receipt',
  },
  {
    name: 'InvoiceItem',
    route: '/invoice-item',
    translationKey: 'global.menu.entities.invoiceItem',
    icon: 'bi bi-receipt-cutoff',
  },
  {
    name: 'StockTransaction',
    route: '/stock-transaction',
    translationKey: 'global.menu.entities.stockTransaction',
    icon: 'bi bi-box',
  },
  {
    name: 'Expense',
    route: '/expense',
    translationKey: 'global.menu.entities.expense',
    icon: 'bi bi-currency-rupee',
  },
  {
    name: 'Employee',
    route: '/employee',
    translationKey: 'global.menu.entities.employee',
    icon: 'bi bi-person-vcard',
  },
];
