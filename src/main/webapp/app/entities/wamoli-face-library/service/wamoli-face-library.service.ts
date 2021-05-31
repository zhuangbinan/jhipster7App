import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWamoliFaceLibrary, getWamoliFaceLibraryIdentifier } from '../wamoli-face-library.model';

export type EntityResponseType = HttpResponse<IWamoliFaceLibrary>;
export type EntityArrayResponseType = HttpResponse<IWamoliFaceLibrary[]>;

@Injectable({ providedIn: 'root' })
export class WamoliFaceLibraryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/wamoli-face-libraries');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(wamoliFaceLibrary: IWamoliFaceLibrary): Observable<EntityResponseType> {
    return this.http.post<IWamoliFaceLibrary>(this.resourceUrl, wamoliFaceLibrary, { observe: 'response' });
  }

  update(wamoliFaceLibrary: IWamoliFaceLibrary): Observable<EntityResponseType> {
    return this.http.put<IWamoliFaceLibrary>(
      `${this.resourceUrl}/${getWamoliFaceLibraryIdentifier(wamoliFaceLibrary) as number}`,
      wamoliFaceLibrary,
      { observe: 'response' }
    );
  }

  partialUpdate(wamoliFaceLibrary: IWamoliFaceLibrary): Observable<EntityResponseType> {
    return this.http.patch<IWamoliFaceLibrary>(
      `${this.resourceUrl}/${getWamoliFaceLibraryIdentifier(wamoliFaceLibrary) as number}`,
      wamoliFaceLibrary,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWamoliFaceLibrary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWamoliFaceLibrary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWamoliFaceLibraryToCollectionIfMissing(
    wamoliFaceLibraryCollection: IWamoliFaceLibrary[],
    ...wamoliFaceLibrariesToCheck: (IWamoliFaceLibrary | null | undefined)[]
  ): IWamoliFaceLibrary[] {
    const wamoliFaceLibraries: IWamoliFaceLibrary[] = wamoliFaceLibrariesToCheck.filter(isPresent);
    if (wamoliFaceLibraries.length > 0) {
      const wamoliFaceLibraryCollectionIdentifiers = wamoliFaceLibraryCollection.map(
        wamoliFaceLibraryItem => getWamoliFaceLibraryIdentifier(wamoliFaceLibraryItem)!
      );
      const wamoliFaceLibrariesToAdd = wamoliFaceLibraries.filter(wamoliFaceLibraryItem => {
        const wamoliFaceLibraryIdentifier = getWamoliFaceLibraryIdentifier(wamoliFaceLibraryItem);
        if (wamoliFaceLibraryIdentifier == null || wamoliFaceLibraryCollectionIdentifiers.includes(wamoliFaceLibraryIdentifier)) {
          return false;
        }
        wamoliFaceLibraryCollectionIdentifiers.push(wamoliFaceLibraryIdentifier);
        return true;
      });
      return [...wamoliFaceLibrariesToAdd, ...wamoliFaceLibraryCollection];
    }
    return wamoliFaceLibraryCollection;
  }
}
