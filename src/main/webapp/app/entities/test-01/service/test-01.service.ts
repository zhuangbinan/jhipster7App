import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITest01, getTest01Identifier } from '../test-01.model';

export type EntityResponseType = HttpResponse<ITest01>;
export type EntityArrayResponseType = HttpResponse<ITest01[]>;

@Injectable({ providedIn: 'root' })
export class Test01Service {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/test-01-s');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(test01: ITest01): Observable<EntityResponseType> {
    return this.http.post<ITest01>(this.resourceUrl, test01, { observe: 'response' });
  }

  update(test01: ITest01): Observable<EntityResponseType> {
    return this.http.put<ITest01>(`${this.resourceUrl}/${getTest01Identifier(test01) as number}`, test01, { observe: 'response' });
  }

  partialUpdate(test01: ITest01): Observable<EntityResponseType> {
    return this.http.patch<ITest01>(`${this.resourceUrl}/${getTest01Identifier(test01) as number}`, test01, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITest01>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITest01[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTest01ToCollectionIfMissing(test01Collection: ITest01[], ...test01sToCheck: (ITest01 | null | undefined)[]): ITest01[] {
    const test01s: ITest01[] = test01sToCheck.filter(isPresent);
    if (test01s.length > 0) {
      const test01CollectionIdentifiers = test01Collection.map(test01Item => getTest01Identifier(test01Item)!);
      const test01sToAdd = test01s.filter(test01Item => {
        const test01Identifier = getTest01Identifier(test01Item);
        if (test01Identifier == null || test01CollectionIdentifiers.includes(test01Identifier)) {
          return false;
        }
        test01CollectionIdentifiers.push(test01Identifier);
        return true;
      });
      return [...test01sToAdd, ...test01Collection];
    }
    return test01Collection;
  }
}
