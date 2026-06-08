import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInventory, NewInventory } from '../inventory.model';

export type PartialUpdateInventory = Partial<IInventory> & Pick<IInventory, 'id'>;

export type EntityResponseType = HttpResponse<IInventory>;
export type EntityArrayResponseType = HttpResponse<IInventory[]>;

@Injectable({ providedIn: 'root' })
export class InventoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stock');

  query(req?: any): Observable<HttpResponse<any[]>> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(`${this.resourceUrl}/stock-details/all`, {
      params: options,
      observe: 'response',
    });
  }

  find(id: number): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.resourceUrl}/stock-details/${id}`, {
      observe: 'response',
    });
  }
}
