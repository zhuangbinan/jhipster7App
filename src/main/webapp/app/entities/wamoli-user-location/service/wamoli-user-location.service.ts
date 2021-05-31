import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWamoliUserLocation, getWamoliUserLocationIdentifier } from '../wamoli-user-location.model';

export type EntityResponseType = HttpResponse<IWamoliUserLocation>;
export type EntityArrayResponseType = HttpResponse<IWamoliUserLocation[]>;

@Injectable({ providedIn: 'root' })
export class WamoliUserLocationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/wamoli-user-locations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(wamoliUserLocation: IWamoliUserLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wamoliUserLocation);
    return this.http
      .post<IWamoliUserLocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(wamoliUserLocation: IWamoliUserLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wamoliUserLocation);
    return this.http
      .put<IWamoliUserLocation>(`${this.resourceUrl}/${getWamoliUserLocationIdentifier(wamoliUserLocation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(wamoliUserLocation: IWamoliUserLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wamoliUserLocation);
    return this.http
      .patch<IWamoliUserLocation>(`${this.resourceUrl}/${getWamoliUserLocationIdentifier(wamoliUserLocation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWamoliUserLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWamoliUserLocation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWamoliUserLocationToCollectionIfMissing(
    wamoliUserLocationCollection: IWamoliUserLocation[],
    ...wamoliUserLocationsToCheck: (IWamoliUserLocation | null | undefined)[]
  ): IWamoliUserLocation[] {
    const wamoliUserLocations: IWamoliUserLocation[] = wamoliUserLocationsToCheck.filter(isPresent);
    if (wamoliUserLocations.length > 0) {
      const wamoliUserLocationCollectionIdentifiers = wamoliUserLocationCollection.map(
        wamoliUserLocationItem => getWamoliUserLocationIdentifier(wamoliUserLocationItem)!
      );
      const wamoliUserLocationsToAdd = wamoliUserLocations.filter(wamoliUserLocationItem => {
        const wamoliUserLocationIdentifier = getWamoliUserLocationIdentifier(wamoliUserLocationItem);
        if (wamoliUserLocationIdentifier == null || wamoliUserLocationCollectionIdentifiers.includes(wamoliUserLocationIdentifier)) {
          return false;
        }
        wamoliUserLocationCollectionIdentifiers.push(wamoliUserLocationIdentifier);
        return true;
      });
      return [...wamoliUserLocationsToAdd, ...wamoliUserLocationCollection];
    }
    return wamoliUserLocationCollection;
  }

  protected convertDateFromClient(wamoliUserLocation: IWamoliUserLocation): IWamoliUserLocation {
    return Object.assign({}, wamoliUserLocation, {
      expireTime: wamoliUserLocation.expireTime?.isValid() ? wamoliUserLocation.expireTime.toJSON() : undefined,
      lastModifiedDate: wamoliUserLocation.lastModifiedDate?.isValid() ? wamoliUserLocation.lastModifiedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expireTime = res.body.expireTime ? dayjs(res.body.expireTime) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? dayjs(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((wamoliUserLocation: IWamoliUserLocation) => {
        wamoliUserLocation.expireTime = wamoliUserLocation.expireTime ? dayjs(wamoliUserLocation.expireTime) : undefined;
        wamoliUserLocation.lastModifiedDate = wamoliUserLocation.lastModifiedDate ? dayjs(wamoliUserLocation.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
