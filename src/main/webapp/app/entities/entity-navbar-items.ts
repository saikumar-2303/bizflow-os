import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Product',
    route: '/product',
    translationKey: 'global.menu.entities.product',
  },
  {
    name: 'Customer',
    route: '/customer',
    translationKey: 'global.menu.entities.customer',
  },
  {
    name: 'CustomerOrder',
    route: '/customer-order',
    translationKey: 'global.menu.entities.customerOrder',
  },
  {
    name: 'OrderItem',
    route: '/order-item',
    translationKey: 'global.menu.entities.orderItem',
  },
  {
    name: 'Invoice',
    route: '/invoice',
    translationKey: 'global.menu.entities.invoice',
  },
  {
    name: 'InvoiceItem',
    route: '/invoice-item',
    translationKey: 'global.menu.entities.invoiceItem',
  },
  {
    name: 'StockTransaction',
    route: '/stock-transaction',
    translationKey: 'global.menu.entities.stockTransaction',
  },
  {
    name: 'Expense',
    route: '/expense',
    translationKey: 'global.menu.entities.expense',
  },
  {
    name: 'Employee',
    route: '/employee',
    translationKey: 'global.menu.entities.employee',
  },
];
