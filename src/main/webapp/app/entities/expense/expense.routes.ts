import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExpenseResolve from './route/expense-routing-resolve.service';

const expenseRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/expense.component').then(m => m.ExpenseComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/expense-detail.component').then(m => m.ExpenseDetailComponent),
    resolve: {
      expense: ExpenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/expense-update.component').then(m => m.ExpenseUpdateComponent),
    resolve: {
      expense: ExpenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/expense-update.component').then(m => m.ExpenseUpdateComponent),
    resolve: {
      expense: ExpenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default expenseRoute;
