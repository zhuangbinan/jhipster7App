import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommunityLeader, getCommunityLeaderIdentifier } from '../community-leader.model';

export type EntityResponseType = HttpResponse<ICommunityLeader>;
export type EntityArrayResponseType = HttpResponse<ICommunityLeader[]>;

@Injectable({ providedIn: 'root' })
export class CommunityLeaderService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/community-leaders');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(communityLeader: ICommunityLeader): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityLeader);
    return this.http
      .post<ICommunityLeader>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(communityLeader: ICommunityLeader): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityLeader);
    return this.http
      .put<ICommunityLeader>(`${this.resourceUrl}/${getCommunityLeaderIdentifier(communityLeader) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(communityLeader: ICommunityLeader): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityLeader);
    return this.http
      .patch<ICommunityLeader>(`${this.resourceUrl}/${getCommunityLeaderIdentifier(communityLeader) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommunityLeader>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommunityLeader[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommunityLeaderToCollectionIfMissing(
    communityLeaderCollection: ICommunityLeader[],
    ...communityLeadersToCheck: (ICommunityLeader | null | undefined)[]
  ): ICommunityLeader[] {
    const communityLeaders: ICommunityLeader[] = communityLeadersToCheck.filter(isPresent);
    if (communityLeaders.length > 0) {
      const communityLeaderCollectionIdentifiers = communityLeaderCollection.map(
        communityLeaderItem => getCommunityLeaderIdentifier(communityLeaderItem)!
      );
      const communityLeadersToAdd = communityLeaders.filter(communityLeaderItem => {
        const communityLeaderIdentifier = getCommunityLeaderIdentifier(communityLeaderItem);
        if (communityLeaderIdentifier == null || communityLeaderCollectionIdentifiers.includes(communityLeaderIdentifier)) {
          return false;
        }
        communityLeaderCollectionIdentifiers.push(communityLeaderIdentifier);
        return true;
      });
      return [...communityLeadersToAdd, ...communityLeaderCollection];
    }
    return communityLeaderCollection;
  }

  protected convertDateFromClient(communityLeader: ICommunityLeader): ICommunityLeader {
    return Object.assign({}, communityLeader, {
      lastModifyDate: communityLeader.lastModifyDate?.isValid() ? communityLeader.lastModifyDate.toJSON() : undefined,
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
      res.body.forEach((communityLeader: ICommunityLeader) => {
        communityLeader.lastModifyDate = communityLeader.lastModifyDate ? dayjs(communityLeader.lastModifyDate) : undefined;
      });
    }
    return res;
  }
}
