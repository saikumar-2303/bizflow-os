import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStockTransaction } from '../stock-transaction.model';
import { StockTransactionService } from '../service/stock-transaction.service';

const stockTransactionResolve = (route: ActivatedRouteSnapshot): Observable<null | IStockTransaction> => {
  const id = route.params.id;
  if (id) {
    return inject(StockTransactionService)
      .find(id)
      .pipe(
        mergeMap((stockTransaction: HttpResponse<IStockTransaction>) => {
          if (stockTransaction.body) {
            return of(stockTransaction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default stockTransactionResolve;
