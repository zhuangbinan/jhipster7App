import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVisitor, getVisitorIdentifier } from '../visitor.model';

export type EntityResponseType = HttpResponse<IVisitor>;
export type EntityArrayResponseType = HttpResponse<IVisitor[]>;

@Injectable({ providedIn: 'root' })
export class VisitorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/visitors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(visitor: IVisitor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visitor);
    return this.http
      .post<IVisitor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(visitor: IVisitor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visitor);
    return this.http
      .put<IVisitor>(`${this.resourceUrl}/${getVisitorIdentifier(visitor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(visitor: IVisitor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visitor);
    return this.http
      .patch<IVisitor>(`${this.resourceUrl}/${getVisitorIdentifier(visitor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVisitor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVisitor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVisitorToCollectionIfMissing(visitorCollection: IVisitor[], ...visitorsToCheck: (IVisitor | null | undefined)[]): IVisitor[] {
    const visitors: IVisitor[] = visitorsToCheck.filter(isPresent);
    if (visitors.length > 0) {
      const visitorCollectionIdentifiers = visitorCollection.map(visitorItem => getVisitorIdentifier(visitorItem)!);
      const visitorsToAdd = visitors.filter(visitorItem => {
        const visitorIdentifier = getVisitorIdentifier(visitorItem);
        if (visitorIdentifier == null || visitorCollectionIdentifiers.includes(visitorIdentifier)) {
          return false;
        }
        visitorCollectionIdentifiers.push(visitorIdentifier);
        return true;
      });
      return [...visitorsToAdd, ...visitorCollection];
    }
    return visitorCollection;
  }

  protected convertDateFromClient(visitor: IVisitor): IVisitor {
    return Object.assign({}, visitor, {
      startTime: visitor.startTime?.isValid() ? visitor.startTime.toJSON() : undefined,
      endTime: visitor.endTime?.isValid() ? visitor.endTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startTime = res.body.startTime ? dayjs(res.body.startTime) : undefined;
      res.body.endTime = res.body.endTime ? dayjs(res.body.endTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((visitor: IVisitor) => {
        visitor.startTime = visitor.startTime ? dayjs(visitor.startTime) : undefined;
        visitor.endTime = visitor.endTime ? dayjs(visitor.endTime) : undefined;
      });
    }
    return res;
  }
}
