import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAuthor, Author } from '../author.model';

import { AuthorService } from './author.service';

describe('Service Tests', () => {
  describe('Author Service', () => {
    let service: AuthorService;
    let httpMock: HttpTestingController;
    let elemDefault: IAuthor;
    let expectedResult: IAuthor | IAuthor[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AuthorService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        birthDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            birthDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Author', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            birthDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Author()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Author', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            birthDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Author', () => {
        const patchObject = Object.assign(
          {
            birthDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new Author()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Author', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            birthDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Author', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAuthorToCollectionIfMissing', () => {
        it('should add a Author to an empty array', () => {
          const author: IAuthor = { id: 123 };
          expectedResult = service.addAuthorToCollectionIfMissing([], author);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(author);
        });

        it('should not add a Author to an array that contains it', () => {
          const author: IAuthor = { id: 123 };
          const authorCollection: IAuthor[] = [
            {
              ...author,
            },
            { id: 456 },
          ];
          expectedResult = service.addAuthorToCollectionIfMissing(authorCollection, author);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Author to an array that doesn't contain it", () => {
          const author: IAuthor = { id: 123 };
          const authorCollection: IAuthor[] = [{ id: 456 }];
          expectedResult = service.addAuthorToCollectionIfMissing(authorCollection, author);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(author);
        });

        it('should add only unique Author to an array', () => {
          const authorArray: IAuthor[] = [{ id: 123 }, { id: 456 }, { id: 53811 }];
          const authorCollection: IAuthor[] = [{ id: 123 }];
          expectedResult = service.addAuthorToCollectionIfMissing(authorCollection, ...authorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const author: IAuthor = { id: 123 };
          const author2: IAuthor = { id: 456 };
          expectedResult = service.addAuthorToCollectionIfMissing([], author, author2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(author);
          expect(expectedResult).toContain(author2);
        });

        it('should accept null and undefined values', () => {
          const author: IAuthor = { id: 123 };
          expectedResult = service.addAuthorToCollectionIfMissing([], null, author, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(author);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
