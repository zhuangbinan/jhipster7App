import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuthor, getAuthorIdentifier } from '../author.model';

export type EntityResponseType = HttpResponse<IAuthor>;
export type EntityArrayResponseType = HttpResponse<IAuthor[]>;

@Injectable({ providedIn: 'root' })
export class AuthorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/authors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(author: IAuthor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(author);
    return this.http
      .post<IAuthor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(author: IAuthor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(author);
    return this.http
      .put<IAuthor>(`${this.resourceUrl}/${getAuthorIdentifier(author) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(author: IAuthor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(author);
    return this.http
      .patch<IAuthor>(`${this.resourceUrl}/${getAuthorIdentifier(author) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAuthor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAuthor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAuthorToCollectionIfMissing(authorCollection: IAuthor[], ...authorsToCheck: (IAuthor | null | undefined)[]): IAuthor[] {
    const authors: IAuthor[] = authorsToCheck.filter(isPresent);
    if (authors.length > 0) {
      const authorCollectionIdentifiers = authorCollection.map(authorItem => getAuthorIdentifier(authorItem)!);
      const authorsToAdd = authors.filter(authorItem => {
        const authorIdentifier = getAuthorIdentifier(authorItem);
        if (authorIdentifier == null || authorCollectionIdentifiers.includes(authorIdentifier)) {
          return false;
        }
        authorCollectionIdentifiers.push(authorIdentifier);
        return true;
      });
      return [...authorsToAdd, ...authorCollection];
    }
    return authorCollection;
  }

  protected convertDateFromClient(author: IAuthor): IAuthor {
    return Object.assign({}, author, {
      birthDate: author.birthDate?.isValid() ? author.birthDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate ? dayjs(res.body.birthDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((author: IAuthor) => {
        author.birthDate = author.birthDate ? dayjs(author.birthDate) : undefined;
      });
    }
    return res;
  }
}
