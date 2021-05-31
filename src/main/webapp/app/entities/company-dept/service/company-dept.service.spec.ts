import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICompanyDept, CompanyDept } from '../company-dept.model';

import { CompanyDeptService } from './company-dept.service';

describe('Service Tests', () => {
  describe('CompanyDept Service', () => {
    let service: CompanyDeptService;
    let httpMock: HttpTestingController;
    let elemDefault: ICompanyDept;
    let expectedResult: ICompanyDept | ICompanyDept[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CompanyDeptService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        parentId: 0,
        ancestors: 'AAAAAAA',
        deptName: 'AAAAAAA',
        orderNum: 0,
        leaderName: 'AAAAAAA',
        tel: 'AAAAAAA',
        email: 'AAAAAAA',
        enable: false,
        delFlag: false,
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

      it('should create a CompanyDept', () => {
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

        service.create(new CompanyDept()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CompanyDept', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            parentId: 1,
            ancestors: 'BBBBBB',
            deptName: 'BBBBBB',
            orderNum: 1,
            leaderName: 'BBBBBB',
            tel: 'BBBBBB',
            email: 'BBBBBB',
            enable: true,
            delFlag: true,
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

      it('should partial update a CompanyDept', () => {
        const patchObject = Object.assign(
          {
            ancestors: 'BBBBBB',
            deptName: 'BBBBBB',
            tel: 'BBBBBB',
            enable: true,
            lastModifyBy: 'BBBBBB',
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new CompanyDept()
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

      it('should return a list of CompanyDept', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            parentId: 1,
            ancestors: 'BBBBBB',
            deptName: 'BBBBBB',
            orderNum: 1,
            leaderName: 'BBBBBB',
            tel: 'BBBBBB',
            email: 'BBBBBB',
            enable: true,
            delFlag: true,
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

      it('should delete a CompanyDept', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCompanyDeptToCollectionIfMissing', () => {
        it('should add a CompanyDept to an empty array', () => {
          const companyDept: ICompanyDept = { id: 123 };
          expectedResult = service.addCompanyDeptToCollectionIfMissing([], companyDept);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(companyDept);
        });

        it('should not add a CompanyDept to an array that contains it', () => {
          const companyDept: ICompanyDept = { id: 123 };
          const companyDeptCollection: ICompanyDept[] = [
            {
              ...companyDept,
            },
            { id: 456 },
          ];
          expectedResult = service.addCompanyDeptToCollectionIfMissing(companyDeptCollection, companyDept);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CompanyDept to an array that doesn't contain it", () => {
          const companyDept: ICompanyDept = { id: 123 };
          const companyDeptCollection: ICompanyDept[] = [{ id: 456 }];
          expectedResult = service.addCompanyDeptToCollectionIfMissing(companyDeptCollection, companyDept);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(companyDept);
        });

        it('should add only unique CompanyDept to an array', () => {
          const companyDeptArray: ICompanyDept[] = [{ id: 123 }, { id: 456 }, { id: 84679 }];
          const companyDeptCollection: ICompanyDept[] = [{ id: 123 }];
          expectedResult = service.addCompanyDeptToCollectionIfMissing(companyDeptCollection, ...companyDeptArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const companyDept: ICompanyDept = { id: 123 };
          const companyDept2: ICompanyDept = { id: 456 };
          expectedResult = service.addCompanyDeptToCollectionIfMissing([], companyDept, companyDept2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(companyDept);
          expect(expectedResult).toContain(companyDept2);
        });

        it('should accept null and undefined values', () => {
          const companyDept: ICompanyDept = { id: 123 };
          expectedResult = service.addCompanyDeptToCollectionIfMissing([], null, companyDept, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(companyDept);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
