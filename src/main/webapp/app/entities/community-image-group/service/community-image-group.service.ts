import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommunityImageGroup, getCommunityImageGroupIdentifier } from '../community-image-group.model';

export type EntityResponseType = HttpResponse<ICommunityImageGroup>;
export type EntityArrayResponseType = HttpResponse<ICommunityImageGroup[]>;

@Injectable({ providedIn: 'root' })
export class CommunityImageGroupService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/community-image-groups');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(communityImageGroup: ICommunityImageGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityImageGroup);
    return this.http
      .post<ICommunityImageGroup>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(communityImageGroup: ICommunityImageGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityImageGroup);
    return this.http
      .put<ICommunityImageGroup>(`${this.resourceUrl}/${getCommunityImageGroupIdentifier(communityImageGroup) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(communityImageGroup: ICommunityImageGroup): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityImageGroup);
    return this.http
      .patch<ICommunityImageGroup>(`${this.resourceUrl}/${getCommunityImageGroupIdentifier(communityImageGroup) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommunityImageGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommunityImageGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommunityImageGroupToCollectionIfMissing(
    communityImageGroupCollection: ICommunityImageGroup[],
    ...communityImageGroupsToCheck: (ICommunityImageGroup | null | undefined)[]
  ): ICommunityImageGroup[] {
    const communityImageGroups: ICommunityImageGroup[] = communityImageGroupsToCheck.filter(isPresent);
    if (communityImageGroups.length > 0) {
      const communityImageGroupCollectionIdentifiers = communityImageGroupCollection.map(
        communityImageGroupItem => getCommunityImageGroupIdentifier(communityImageGroupItem)!
      );
      const communityImageGroupsToAdd = communityImageGroups.filter(communityImageGroupItem => {
        const communityImageGroupIdentifier = getCommunityImageGroupIdentifier(communityImageGroupItem);
        if (communityImageGroupIdentifier == null || communityImageGroupCollectionIdentifiers.includes(communityImageGroupIdentifier)) {
          return false;
        }
        communityImageGroupCollectionIdentifiers.push(communityImageGroupIdentifier);
        return true;
      });
      return [...communityImageGroupsToAdd, ...communityImageGroupCollection];
    }
    return communityImageGroupCollection;
  }

  protected convertDateFromClient(communityImageGroup: ICommunityImageGroup): ICommunityImageGroup {
    return Object.assign({}, communityImageGroup, {
      lastModifyDate: communityImageGroup.lastModifyDate?.isValid() ? communityImageGroup.lastModifyDate.toJSON() : undefined,
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
      res.body.forEach((communityImageGroup: ICommunityImageGroup) => {
        communityImageGroup.lastModifyDate = communityImageGroup.lastModifyDate ? dayjs(communityImageGroup.lastModifyDate) : undefined;
      });
    }
    return res;
  }
}
