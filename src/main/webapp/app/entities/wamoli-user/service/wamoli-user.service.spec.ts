import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { CertificateType } from 'app/entities/enumerations/certificate-type.model';
import { UserType } from 'app/entities/enumerations/user-type.model';
import { IWamoliUser, WamoliUser } from '../wamoli-user.model';

import { WamoliUserService } from './wamoli-user.service';

describe('Service Tests', () => {
  describe('WamoliUser Service', () => {
    let service: WamoliUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IWamoliUser;
    let expectedResult: IWamoliUser | IWamoliUser[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WamoliUserService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        userName: 'AAAAAAA',
        gender: 'AAAAAAA',
        phoneNum: 'AAAAAAA',
        email: 'AAAAAAA',
        unitAddr: 'AAAAAAA',
        roomAddr: 0,
        idCardNum: 'AAAAAAA',
        idCardType: CertificateType.IDCARD,
        userType: UserType.OWNER,
        enable: false,
        isManager: false,
        lastModifiedBy: 'AAAAAAA',
        lastModifiedDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a WamoliUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new WamoliUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WamoliUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            userName: 'BBBBBB',
            gender: 'BBBBBB',
            phoneNum: 'BBBBBB',
            email: 'BBBBBB',
            unitAddr: 'BBBBBB',
            roomAddr: 1,
            idCardNum: 'BBBBBB',
            idCardType: 'BBBBBB',
            userType: 'BBBBBB',
            enable: true,
            isManager: true,
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WamoliUser', () => {
        const patchObject = Object.assign(
          {
            userName: 'BBBBBB',
            email: 'BBBBBB',
            roomAddr: 1,
            idCardType: 'BBBBBB',
            userType: 'BBBBBB',
            enable: true,
            isManager: true,
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new WamoliUser()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WamoliUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            userName: 'BBBBBB',
            gender: 'BBBBBB',
            phoneNum: 'BBBBBB',
            email: 'BBBBBB',
            unitAddr: 'BBBBBB',
            roomAddr: 1,
            idCardNum: 'BBBBBB',
            idCardType: 'BBBBBB',
            userType: 'BBBBBB',
            enable: true,
            isManager: true,
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a WamoliUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWamoliUserToCollectionIfMissing', () => {
        it('should add a WamoliUser to an empty array', () => {
          const wamoliUser: IWamoliUser = { id: 123 };
          expectedResult = service.addWamoliUserToCollectionIfMissing([], wamoliUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wamoliUser);
        });

        it('should not add a WamoliUser to an array that contains it', () => {
          const wamoliUser: IWamoliUser = { id: 123 };
          const wamoliUserCollection: IWamoliUser[] = [
            {
              ...wamoliUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addWamoliUserToCollectionIfMissing(wamoliUserCollection, wamoliUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WamoliUser to an array that doesn't contain it", () => {
          const wamoliUser: IWamoliUser = { id: 123 };
          const wamoliUserCollection: IWamoliUser[] = [{ id: 456 }];
          expectedResult = service.addWamoliUserToCollectionIfMissing(wamoliUserCollection, wamoliUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wamoliUser);
        });

        it('should add only unique WamoliUser to an array', () => {
          const wamoliUserArray: IWamoliUser[] = [{ id: 123 }, { id: 456 }, { id: 31584 }];
          const wamoliUserCollection: IWamoliUser[] = [{ id: 123 }];
          expectedResult = service.addWamoliUserToCollectionIfMissing(wamoliUserCollection, ...wamoliUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const wamoliUser: IWamoliUser = { id: 123 };
          const wamoliUser2: IWamoliUser = { id: 456 };
          expectedResult = service.addWamoliUserToCollectionIfMissing([], wamoliUser, wamoliUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wamoliUser);
          expect(expectedResult).toContain(wamoliUser2);
        });

        it('should accept null and undefined values', () => {
          const wamoliUser: IWamoliUser = { id: 123 };
          expectedResult = service.addWamoliUserToCollectionIfMissing([], null, wamoliUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wamoliUser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
