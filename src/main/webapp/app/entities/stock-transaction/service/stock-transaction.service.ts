import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStockTransaction, NewStockTransaction } from '../stock-transaction.model';

export type PartialUpdateStockTransaction = Partial<IStockTransaction> & Pick<IStockTransaction, 'id'>;

type RestOf<T extends IStockTransaction | NewStockTransaction> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

export type RestStockTransaction = RestOf<IStockTransaction>;

export type NewRestStockTransaction = RestOf<NewStockTransaction>;

export type PartialUpdateRestStockTransaction = RestOf<PartialUpdateStockTransaction>;

export type EntityResponseType = HttpResponse<IStockTransaction>;
export type EntityArrayResponseType = HttpResponse<IStockTransaction[]>;

@Injectable({ providedIn: 'root' })
export class StockTransactionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stock-transactions');

  create(stockTransaction: NewStockTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockTransaction);
    return this.http
      .post<RestStockTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(stockTransaction: IStockTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockTransaction);
    return this.http
      .put<RestStockTransaction>(`${this.resourceUrl}/${this.getStockTransactionIdentifier(stockTransaction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(stockTransaction: PartialUpdateStockTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockTransaction);
    return this.http
      .patch<RestStockTransaction>(`${this.resourceUrl}/${this.getStockTransactionIdentifier(stockTransaction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestStockTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestStockTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStockTransactionIdentifier(stockTransaction: Pick<IStockTransaction, 'id'>): number {
    return stockTransaction.id;
  }

  compareStockTransaction(o1: Pick<IStockTransaction, 'id'> | null, o2: Pick<IStockTransaction, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockTransactionIdentifier(o1) === this.getStockTransactionIdentifier(o2) : o1 === o2;
  }

  addStockTransactionToCollectionIfMissing<Type extends Pick<IStockTransaction, 'id'>>(
    stockTransactionCollection: Type[],
    ...stockTransactionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stockTransactions: Type[] = stockTransactionsToCheck.filter(isPresent);
    if (stockTransactions.length > 0) {
      const stockTransactionCollectionIdentifiers = stockTransactionCollection.map(stockTransactionItem =>
        this.getStockTransactionIdentifier(stockTransactionItem),
      );
      const stockTransactionsToAdd = stockTransactions.filter(stockTransactionItem => {
        const stockTransactionIdentifier = this.getStockTransactionIdentifier(stockTransactionItem);
        if (stockTransactionCollectionIdentifiers.includes(stockTransactionIdentifier)) {
          return false;
        }
        stockTransactionCollectionIdentifiers.push(stockTransactionIdentifier);
        return true;
      });
      return [...stockTransactionsToAdd, ...stockTransactionCollection];
    }
    return stockTransactionCollection;
  }

  protected convertDateFromClient<T extends IStockTransaction | NewStockTransaction | PartialUpdateStockTransaction>(
    stockTransaction: T,
  ): RestOf<T> {
    return {
      ...stockTransaction,
      createdDate: stockTransaction.createdDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restStockTransaction: RestStockTransaction): IStockTransaction {
    return {
      ...restStockTransaction,
      createdDate: restStockTransaction.createdDate ? dayjs(restStockTransaction.createdDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestStockTransaction>): HttpResponse<IStockTransaction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestStockTransaction[]>): HttpResponse<IStockTransaction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
