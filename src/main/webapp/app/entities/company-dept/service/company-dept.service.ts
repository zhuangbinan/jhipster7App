import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompanyDept, getCompanyDeptIdentifier } from '../company-dept.model';

export type EntityResponseType = HttpResponse<ICompanyDept>;
export type EntityArrayResponseType = HttpResponse<ICompanyDept[]>;

@Injectable({ providedIn: 'root' })
export class CompanyDeptService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/company-depts');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(companyDept: ICompanyDept): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyDept);
    return this.http
      .post<ICompanyDept>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(companyDept: ICompanyDept): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyDept);
    return this.http
      .put<ICompanyDept>(`${this.resourceUrl}/${getCompanyDeptIdentifier(companyDept) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(companyDept: ICompanyDept): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyDept);
    return this.http
      .patch<ICompanyDept>(`${this.resourceUrl}/${getCompanyDeptIdentifier(companyDept) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompanyDept>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompanyDept[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompanyDeptToCollectionIfMissing(
    companyDeptCollection: ICompanyDept[],
    ...companyDeptsToCheck: (ICompanyDept | null | undefined)[]
  ): ICompanyDept[] {
    const companyDepts: ICompanyDept[] = companyDeptsToCheck.filter(isPresent);
    if (companyDepts.length > 0) {
      const companyDeptCollectionIdentifiers = companyDeptCollection.map(companyDeptItem => getCompanyDeptIdentifier(companyDeptItem)!);
      const companyDeptsToAdd = companyDepts.filter(companyDeptItem => {
        const companyDeptIdentifier = getCompanyDeptIdentifier(companyDeptItem);
        if (companyDeptIdentifier == null || companyDeptCollectionIdentifiers.includes(companyDeptIdentifier)) {
          return false;
        }
        companyDeptCollectionIdentifiers.push(companyDeptIdentifier);
        return true;
      });
      return [...companyDeptsToAdd, ...companyDeptCollection];
    }
    return companyDeptCollection;
  }

  protected convertDateFromClient(companyDept: ICompanyDept): ICompanyDept {
    return Object.assign({}, companyDept, {
      createDate: companyDept.createDate?.isValid() ? companyDept.createDate.toJSON() : undefined,
      lastModifyDate: companyDept.lastModifyDate?.isValid() ? companyDept.lastModifyDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.lastModifyDate = res.body.lastModifyDate ? dayjs(res.body.lastModifyDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((companyDept: ICompanyDept) => {
        companyDept.createDate = companyDept.createDate ? dayjs(companyDept.createDate) : undefined;
        companyDept.lastModifyDate = companyDept.lastModifyDate ? dayjs(companyDept.lastModifyDate) : undefined;
      });
    }
    return res;
  }
}
