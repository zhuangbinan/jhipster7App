import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommunityNotice, getCommunityNoticeIdentifier } from '../community-notice.model';

export type EntityResponseType = HttpResponse<ICommunityNotice>;
export type EntityArrayResponseType = HttpResponse<ICommunityNotice[]>;

@Injectable({ providedIn: 'root' })
export class CommunityNoticeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/community-notices');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(communityNotice: ICommunityNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityNotice);
    return this.http
      .post<ICommunityNotice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(communityNotice: ICommunityNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityNotice);
    return this.http
      .put<ICommunityNotice>(`${this.resourceUrl}/${getCommunityNoticeIdentifier(communityNotice) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(communityNotice: ICommunityNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityNotice);
    return this.http
      .patch<ICommunityNotice>(`${this.resourceUrl}/${getCommunityNoticeIdentifier(communityNotice) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommunityNotice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommunityNotice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommunityNoticeToCollectionIfMissing(
    communityNoticeCollection: ICommunityNotice[],
    ...communityNoticesToCheck: (ICommunityNotice | null | undefined)[]
  ): ICommunityNotice[] {
    const communityNotices: ICommunityNotice[] = communityNoticesToCheck.filter(isPresent);
    if (communityNotices.length > 0) {
      const communityNoticeCollectionIdentifiers = communityNoticeCollection.map(
        communityNoticeItem => getCommunityNoticeIdentifier(communityNoticeItem)!
      );
      const communityNoticesToAdd = communityNotices.filter(communityNoticeItem => {
        const communityNoticeIdentifier = getCommunityNoticeIdentifier(communityNoticeItem);
        if (communityNoticeIdentifier == null || communityNoticeCollectionIdentifiers.includes(communityNoticeIdentifier)) {
          return false;
        }
        communityNoticeCollectionIdentifiers.push(communityNoticeIdentifier);
        return true;
      });
      return [...communityNoticesToAdd, ...communityNoticeCollection];
    }
    return communityNoticeCollection;
  }

  protected convertDateFromClient(communityNotice: ICommunityNotice): ICommunityNotice {
    return Object.assign({}, communityNotice, {
      lastModifyDate: communityNotice.lastModifyDate?.isValid() ? communityNotice.lastModifyDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastModifyDate = res.body.lastModifyDate ? dayjs(res.body.lastModifyDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((communityNotice: ICommunityNotice) => {
        communityNotice.lastModifyDate = communityNotice.lastModifyDate ? dayjs(communityNotice.lastModifyDate) : undefined;
      });
    }
    return res;
  }
}
