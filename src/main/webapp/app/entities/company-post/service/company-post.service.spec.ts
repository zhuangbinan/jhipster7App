import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICompanyPost, CompanyPost } from '../company-post.model';

import { CompanyPostService } from './company-post.service';

describe('Service Tests', () => {
  describe('CompanyPost Service', () => {
    let service: CompanyPostService;
    let httpMock: HttpTestingController;
    let elemDefault: ICompanyPost;
    let expectedResult: ICompanyPost | ICompanyPost[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CompanyPostService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        postCode: 'AAAAAAA',
        postName: 'AAAAAAA',
        orderNum: 0,
        remark: 'AAAAAAA',
        enable: false,
        createBy: 'AAAAAAA',
        createDate: currentDate,
        lastModifyBy: 'AAAAAAA',
        lastModifyDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CompanyPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModifyDate: currentDate,
          },
          returnedFromService
        );

        service.create(new CompanyPost()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CompanyPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            postCode: 'BBBBBB',
            postName: 'BBBBBB',
            orderNum: 1,
            remark: 'BBBBBB',
            enable: true,
            createBy: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyBy: 'BBBBBB',
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModifyDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CompanyPost', () => {
        const patchObject = Object.assign(
          {
            postCode: 'BBBBBB',
            postName: 'BBBBBB',
            orderNum: 1,
            enable: true,
            createBy: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyBy: 'BBBBBB',
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new CompanyPost()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModifyDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CompanyPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            postCode: 'BBBBBB',
            postName: 'BBBBBB',
            orderNum: 1,
            remark: 'BBBBBB',
            enable: true,
            createBy: 'BBBBBB',
            createDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyBy: 'BBBBBB',
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createDate: currentDate,
            lastModifyDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CompanyPost', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCompanyPostToCollectionIfMissing', () => {
        it('should add a CompanyPost to an empty array', () => {
          const companyPost: ICompanyPost = { id: 123 };
          expectedResult = service.addCompanyPostToCollectionIfMissing([], companyPost);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(companyPost);
        });

        it('should not add a CompanyPost to an array that contains it', () => {
          const companyPost: ICompanyPost = { id: 123 };
          const companyPostCollection: ICompanyPost[] = [
            {
              ...companyPost,
            },
            { id: 456 },
          ];
          expectedResult = service.addCompanyPostToCollectionIfMissing(companyPostCollection, companyPost);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CompanyPost to an array that doesn't contain it", () => {
          const companyPost: ICompanyPost = { id: 123 };
          const companyPostCollection: ICompanyPost[] = [{ id: 456 }];
          expectedResult = service.addCompanyPostToCollectionIfMissing(companyPostCollection, companyPost);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(companyPost);
        });

        it('should add only unique CompanyPost to an array', () => {
          const companyPostArray: ICompanyPost[] = [{ id: 123 }, { id: 456 }, { id: 91522 }];
          const companyPostCollection: ICompanyPost[] = [{ id: 123 }];
          expectedResult = service.addCompanyPostToCollectionIfMissing(companyPostCollection, ...companyPostArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const companyPost: ICompanyPost = { id: 123 };
          const companyPost2: ICompanyPost = { id: 456 };
          expectedResult = service.addCompanyPostToCollectionIfMissing([], companyPost, companyPost2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(companyPost);
          expect(expectedResult).toContain(companyPost2);
        });

        it('should accept null and undefined values', () => {
          const companyPost: ICompanyPost = { id: 123 };
          expectedResult = service.addCompanyPostToCollectionIfMissing([], null, companyPost, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(companyPost);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
