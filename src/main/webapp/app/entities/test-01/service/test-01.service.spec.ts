import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITest01, Test01 } from '../test-01.model';

import { Test01Service } from './test-01.service';

describe('Service Tests', () => {
  describe('Test01 Service', () => {
    let service: Test01Service;
    let httpMock: HttpTestingController;
    let elemDefault: ITest01;
    let expectedResult: ITest01 | ITest01[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(Test01Service);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        jobCareerDesc: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Test01', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Test01()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Test01', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            jobCareerDesc: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Test01', () => {
        const patchObject = Object.assign(
          {
            jobCareerDesc: 'BBBBBB',
          },
          new Test01()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Test01', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            jobCareerDesc: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Test01', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTest01ToCollectionIfMissing', () => {
        it('should add a Test01 to an empty array', () => {
          const test01: ITest01 = { id: 123 };
          expectedResult = service.addTest01ToCollectionIfMissing([], test01);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(test01);
        });

        it('should not add a Test01 to an array that contains it', () => {
          const test01: ITest01 = { id: 123 };
          const test01Collection: ITest01[] = [
            {
              ...test01,
            },
            { id: 456 },
          ];
          expectedResult = service.addTest01ToCollectionIfMissing(test01Collection, test01);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Test01 to an array that doesn't contain it", () => {
          const test01: ITest01 = { id: 123 };
          const test01Collection: ITest01[] = [{ id: 456 }];
          expectedResult = service.addTest01ToCollectionIfMissing(test01Collection, test01);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(test01);
        });

        it('should add only unique Test01 to an array', () => {
          const test01Array: ITest01[] = [{ id: 123 }, { id: 456 }, { id: 68693 }];
          const test01Collection: ITest01[] = [{ id: 123 }];
          expectedResult = service.addTest01ToCollectionIfMissing(test01Collection, ...test01Array);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const test01: ITest01 = { id: 123 };
          const test012: ITest01 = { id: 456 };
          expectedResult = service.addTest01ToCollectionIfMissing([], test01, test012);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(test01);
          expect(expectedResult).toContain(test012);
        });

        it('should accept null and undefined values', () => {
          const test01: ITest01 = { id: 123 };
          expectedResult = service.addTest01ToCollectionIfMissing([], null, test01, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(test01);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
