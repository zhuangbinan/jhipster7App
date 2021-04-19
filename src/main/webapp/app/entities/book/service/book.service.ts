import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBook, getBookIdentifier } from '../book.model';

export type EntityResponseType = HttpResponse<IBook>;
export type EntityArrayResponseType = HttpResponse<IBook[]>;

@Injectable({ providedIn: 'root' })
export class BookService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/books');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(book: IBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(book);
    return this.http
      .post<IBook>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(book: IBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(book);
    return this.http
      .put<IBook>(`${this.resourceUrl}/${getBookIdentifier(book) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(book: IBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(book);
    return this.http
      .patch<IBook>(`${this.resourceUrl}/${getBookIdentifier(book) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBook>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBookToCollectionIfMissing(bookCollection: IBook[], ...booksToCheck: (IBook | null | undefined)[]): IBook[] {
    const books: IBook[] = booksToCheck.filter(isPresent);
    if (books.length > 0) {
      const bookCollectionIdentifiers = bookCollection.map(bookItem => getBookIdentifier(bookItem)!);
      const booksToAdd = books.filter(bookItem => {
        const bookIdentifier = getBookIdentifier(bookItem);
        if (bookIdentifier == null || bookCollectionIdentifiers.includes(bookIdentifier)) {
          return false;
        }
        bookCollectionIdentifiers.push(bookIdentifier);
        return true;
      });
      return [...booksToAdd, ...bookCollection];
    }
    return bookCollection;
  }

  protected convertDateFromClient(book: IBook): IBook {
    return Object.assign({}, book, {
      publisherDate: book.publisherDate?.isValid() ? book.publisherDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.publisherDate = res.body.publisherDate ? dayjs(res.body.publisherDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((book: IBook) => {
        book.publisherDate = book.publisherDate ? dayjs(book.publisherDate) : undefined;
      });
    }
    return res;
  }
}
