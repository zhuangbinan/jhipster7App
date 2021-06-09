import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompanyUser, getCompanyUserIdentifier } from '../company-user.model';

export type EntityResponseType = HttpResponse<ICompanyUser>;
export type EntityArrayResponseType = HttpResponse<ICompanyUser[]>;

@Injectable({ providedIn: 'root' })
export class CompanyUserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/company-users');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(companyUser: ICompanyUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyUser);
    return this.http
      .post<ICompanyUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(companyUser: ICompanyUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyUser);
    return this.http
      .put<ICompanyUser>(`${this.resourceUrl}/${getCompanyUserIdentifier(companyUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(companyUser: ICompanyUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyUser);
    return this.http
      .patch<ICompanyUser>(`${this.resourceUrl}/${getCompanyUserIdentifier(companyUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompanyUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompanyUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompanyUserToCollectionIfMissing(
    companyUserCollection: ICompanyUser[],
    ...companyUsersToCheck: (ICompanyUser | null | undefined)[]
  ): ICompanyUser[] {
    const companyUsers: ICompanyUser[] = companyUsersToCheck.filter(isPresent);
    if (companyUsers.length > 0) {
      const companyUserCollectionIdentifiers = companyUserCollection.map(companyUserItem => getCompanyUserIdentifier(companyUserItem)!);
      const companyUsersToAdd = companyUsers.filter(companyUserItem => {
        const companyUserIdentifier = getCompanyUserIdentifier(companyUserItem);
        if (companyUserIdentifier == null || companyUserCollectionIdentifiers.includes(companyUserIdentifier)) {
          return false;
        }
        companyUserCollectionIdentifiers.push(companyUserIdentifier);
        return true;
      });
      return [...companyUsersToAdd, ...companyUserCollection];
    }
    return companyUserCollection;
  }

  protected convertDateFromClient(companyUser: ICompanyUser): ICompanyUser {
    return Object.assign({}, companyUser, {
      createdDate: companyUser.createdDate?.isValid() ? companyUser.createdDate.toJSON() : undefined,
      lastModifiedDate: companyUser.lastModifiedDate?.isValid() ? companyUser.lastModifiedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? dayjs(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((companyUser: ICompanyUser) => {
        companyUser.createdDate = companyUser.createdDate ? dayjs(companyUser.createdDate) : undefined;
        companyUser.lastModifiedDate = companyUser.lastModifiedDate ? dayjs(companyUser.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
