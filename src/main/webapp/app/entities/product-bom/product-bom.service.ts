import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IProductBom, NewProductBom } from './product-bom.model';

export type PartialUpdateProductBom = Partial<IProductBom> & Pick<IProductBom, 'id'>;
export type EntityResponseType = HttpResponse<IProductBom>;
export type EntityArrayResponseType = HttpResponse<IProductBom[]>;

@Injectable({ providedIn: 'root' })
export class ProductBomService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  // Maps to your JHipster backend Bill of Materials endpoints
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-bom');

  create(productBom: NewProductBom): Observable<EntityResponseType> {
    return this.http.post<IProductBom>(`${this.resourceUrl}/create`, productBom, { observe: 'response' });
  }
}
