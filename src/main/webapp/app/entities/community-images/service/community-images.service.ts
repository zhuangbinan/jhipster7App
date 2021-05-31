import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommunityImages, getCommunityImagesIdentifier } from '../community-images.model';

export type EntityResponseType = HttpResponse<ICommunityImages>;
export type EntityArrayResponseType = HttpResponse<ICommunityImages[]>;

@Injectable({ providedIn: 'root' })
export class CommunityImagesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/community-images');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(communityImages: ICommunityImages): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityImages);
    return this.http
      .post<ICommunityImages>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(communityImages: ICommunityImages): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityImages);
    return this.http
      .put<ICommunityImages>(`${this.resourceUrl}/${getCommunityImagesIdentifier(communityImages) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(communityImages: ICommunityImages): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(communityImages);
    return this.http
      .patch<ICommunityImages>(`${this.resourceUrl}/${getCommunityImagesIdentifier(communityImages) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommunityImages>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommunityImages[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommunityImagesToCollectionIfMissing(
    communityImagesCollection: ICommunityImages[],
    ...communityImagesToCheck: (ICommunityImages | null | undefined)[]
  ): ICommunityImages[] {
    const communityImages: ICommunityImages[] = communityImagesToCheck.filter(isPresent);
    if (communityImages.length > 0) {
      const communityImagesCollectionIdentifiers = communityImagesCollection.map(
        communityImagesItem => getCommunityImagesIdentifier(communityImagesItem)!
      );
      const communityImagesToAdd = communityImages.filter(communityImagesItem => {
        const communityImagesIdentifier = getCommunityImagesIdentifier(communityImagesItem);
        if (communityImagesIdentifier == null || communityImagesCollectionIdentifiers.includes(communityImagesIdentifier)) {
          return false;
        }
        communityImagesCollectionIdentifiers.push(communityImagesIdentifier);
        return true;
      });
      return [...communityImagesToAdd, ...communityImagesCollection];
    }
    return communityImagesCollection;
  }

  protected convertDateFromClient(communityImages: ICommunityImages): ICommunityImages {
    return Object.assign({}, communityImages, {
      lastModifyDate: communityImages.lastModifyDate?.isValid() ? communityImages.lastModifyDate.toJSON() : undefined,
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
      res.body.forEach((communityImages: ICommunityImages) => {
        communityImages.lastModifyDate = communityImages.lastModifyDate ? dayjs(communityImages.lastModifyDate) : undefined;
      });
    }
    return res;
  }
}
