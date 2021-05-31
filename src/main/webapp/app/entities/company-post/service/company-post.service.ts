import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompanyPost, getCompanyPostIdentifier } from '../company-post.model';

export type EntityResponseType = HttpResponse<ICompanyPost>;
export type EntityArrayResponseType = HttpResponse<ICompanyPost[]>;

@Injectable({ providedIn: 'root' })
export class CompanyPostService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/company-posts');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(companyPost: ICompanyPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyPost);
    return this.http
      .post<ICompanyPost>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(companyPost: ICompanyPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyPost);
    return this.http
      .put<ICompanyPost>(`${this.resourceUrl}/${getCompanyPostIdentifier(companyPost) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(companyPost: ICompanyPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyPost);
    return this.http
      .patch<ICompanyPost>(`${this.resourceUrl}/${getCompanyPostIdentifier(companyPost) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompanyPost>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompanyPost[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompanyPostToCollectionIfMissing(
    companyPostCollection: ICompanyPost[],
    ...companyPostsToCheck: (ICompanyPost | null | undefined)[]
  ): ICompanyPost[] {
    const companyPosts: ICompanyPost[] = companyPostsToCheck.filter(isPresent);
    if (companyPosts.length > 0) {
      const companyPostCollectionIdentifiers = companyPostCollection.map(companyPostItem => getCompanyPostIdentifier(companyPostItem)!);
      const companyPostsToAdd = companyPosts.filter(companyPostItem => {
        const companyPostIdentifier = getCompanyPostIdentifier(companyPostItem);
        if (companyPostIdentifier == null || companyPostCollectionIdentifiers.includes(companyPostIdentifier)) {
          return false;
        }
        companyPostCollectionIdentifiers.push(companyPostIdentifier);
        return true;
      });
      return [...companyPostsToAdd, ...companyPostCollection];
    }
    return companyPostCollection;
  }

  protected convertDateFromClient(companyPost: ICompanyPost): ICompanyPost {
    return Object.assign({}, companyPost, {
      createDate: companyPost.createDate?.isValid() ? companyPost.createDate.toJSON() : undefined,
      lastModifyDate: companyPost.lastModifyDate?.isValid() ? companyPost.lastModifyDate.toJSON() : undefined,
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
      res.body.forEach((companyPost: ICompanyPost) => {
        companyPost.createDate = companyPost.createDate ? dayjs(companyPost.createDate) : undefined;
        companyPost.lastModifyDate = companyPost.lastModifyDate ? dayjs(companyPost.lastModifyDate) : undefined;
      });
    }
    return res;
  }
}
