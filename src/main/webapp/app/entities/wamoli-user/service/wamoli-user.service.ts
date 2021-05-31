import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWamoliUser, getWamoliUserIdentifier } from '../wamoli-user.model';

export type EntityResponseType = HttpResponse<IWamoliUser>;
export type EntityArrayResponseType = HttpResponse<IWamoliUser[]>;

@Injectable({ providedIn: 'root' })
export class WamoliUserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/wamoli-users');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(wamoliUser: IWamoliUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wamoliUser);
    return this.http
      .post<IWamoliUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(wamoliUser: IWamoliUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wamoliUser);
    return this.http
      .put<IWamoliUser>(`${this.resourceUrl}/${getWamoliUserIdentifier(wamoliUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(wamoliUser: IWamoliUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wamoliUser);
    return this.http
      .patch<IWamoliUser>(`${this.resourceUrl}/${getWamoliUserIdentifier(wamoliUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWamoliUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWamoliUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWamoliUserToCollectionIfMissing(
    wamoliUserCollection: IWamoliUser[],
    ...wamoliUsersToCheck: (IWamoliUser | null | undefined)[]
  ): IWamoliUser[] {
    const wamoliUsers: IWamoliUser[] = wamoliUsersToCheck.filter(isPresent);
    if (wamoliUsers.length > 0) {
      const wamoliUserCollectionIdentifiers = wamoliUserCollection.map(wamoliUserItem => getWamoliUserIdentifier(wamoliUserItem)!);
      const wamoliUsersToAdd = wamoliUsers.filter(wamoliUserItem => {
        const wamoliUserIdentifier = getWamoliUserIdentifier(wamoliUserItem);
        if (wamoliUserIdentifier == null || wamoliUserCollectionIdentifiers.includes(wamoliUserIdentifier)) {
          return false;
        }
        wamoliUserCollectionIdentifiers.push(wamoliUserIdentifier);
        return true;
      });
      return [...wamoliUsersToAdd, ...wamoliUserCollection];
    }
    return wamoliUserCollection;
  }

  protected convertDateFromClient(wamoliUser: IWamoliUser): IWamoliUser {
    return Object.assign({}, wamoliUser, {
      lastModifiedDate: wamoliUser.lastModifiedDate?.isValid() ? wamoliUser.lastModifiedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastModifiedDate = res.body.lastModifiedDate ? dayjs(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((wamoliUser: IWamoliUser) => {
        wamoliUser.lastModifiedDate = wamoliUser.lastModifiedDate ? dayjs(wamoliUser.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
