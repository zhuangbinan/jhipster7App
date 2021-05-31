import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBuildings, getBuildingsIdentifier } from '../buildings.model';

export type EntityResponseType = HttpResponse<IBuildings>;
export type EntityArrayResponseType = HttpResponse<IBuildings[]>;

@Injectable({ providedIn: 'root' })
export class BuildingsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/buildings');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(buildings: IBuildings): Observable<EntityResponseType> {
    return this.http.post<IBuildings>(this.resourceUrl, buildings, { observe: 'response' });
  }

  update(buildings: IBuildings): Observable<EntityResponseType> {
    return this.http.put<IBuildings>(`${this.resourceUrl}/${getBuildingsIdentifier(buildings) as number}`, buildings, {
      observe: 'response',
    });
  }

  partialUpdate(buildings: IBuildings): Observable<EntityResponseType> {
    return this.http.patch<IBuildings>(`${this.resourceUrl}/${getBuildingsIdentifier(buildings) as number}`, buildings, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBuildings>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBuildings[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBuildingsToCollectionIfMissing(
    buildingsCollection: IBuildings[],
    ...buildingsToCheck: (IBuildings | null | undefined)[]
  ): IBuildings[] {
    const buildings: IBuildings[] = buildingsToCheck.filter(isPresent);
    if (buildings.length > 0) {
      const buildingsCollectionIdentifiers = buildingsCollection.map(buildingsItem => getBuildingsIdentifier(buildingsItem)!);
      const buildingsToAdd = buildings.filter(buildingsItem => {
        const buildingsIdentifier = getBuildingsIdentifier(buildingsItem);
        if (buildingsIdentifier == null || buildingsCollectionIdentifiers.includes(buildingsIdentifier)) {
          return false;
        }
        buildingsCollectionIdentifiers.push(buildingsIdentifier);
        return true;
      });
      return [...buildingsToAdd, ...buildingsCollection];
    }
    return buildingsCollection;
  }
}
