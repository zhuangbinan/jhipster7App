import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWamoliUserLocation, WamoliUserLocation } from '../wamoli-user-location.model';

import { WamoliUserLocationService } from './wamoli-user-location.service';

describe('Service Tests', () => {
  describe('WamoliUserLocation Service', () => {
    let service: WamoliUserLocationService;
    let httpMock: HttpTestingController;
    let elemDefault: IWamoliUserLocation;
    let expectedResult: IWamoliUserLocation | IWamoliUserLocation[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WamoliUserLocationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        state: 0,
        cardNum: 'AAAAAAA',
        expireTime: currentDate,
        delayTime: 0,
        lastModifiedBy: 'AAAAAAA',
        lastModifiedDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            expireTime: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a WamoliUserLocation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            expireTime: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expireTime: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new WamoliUserLocation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WamoliUserLocation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            state: 1,
            cardNum: 'BBBBBB',
            expireTime: currentDate.format(DATE_TIME_FORMAT),
            delayTime: 1,
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expireTime: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WamoliUserLocation', () => {
        const patchObject = Object.assign(
          {
            state: 1,
            expireTime: currentDate.format(DATE_TIME_FORMAT),
            delayTime: 1,
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new WamoliUserLocation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            expireTime: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WamoliUserLocation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            state: 1,
            cardNum: 'BBBBBB',
            expireTime: currentDate.format(DATE_TIME_FORMAT),
            delayTime: 1,
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expireTime: currentDate,
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

      it('should delete a WamoliUserLocation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWamoliUserLocationToCollectionIfMissing', () => {
        it('should add a WamoliUserLocation to an empty array', () => {
          const wamoliUserLocation: IWamoliUserLocation = { id: 123 };
          expectedResult = service.addWamoliUserLocationToCollectionIfMissing([], wamoliUserLocation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wamoliUserLocation);
        });

        it('should not add a WamoliUserLocation to an array that contains it', () => {
          const wamoliUserLocation: IWamoliUserLocation = { id: 123 };
          const wamoliUserLocationCollection: IWamoliUserLocation[] = [
            {
              ...wamoliUserLocation,
            },
            { id: 456 },
          ];
          expectedResult = service.addWamoliUserLocationToCollectionIfMissing(wamoliUserLocationCollection, wamoliUserLocation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WamoliUserLocation to an array that doesn't contain it", () => {
          const wamoliUserLocation: IWamoliUserLocation = { id: 123 };
          const wamoliUserLocationCollection: IWamoliUserLocation[] = [{ id: 456 }];
          expectedResult = service.addWamoliUserLocationToCollectionIfMissing(wamoliUserLocationCollection, wamoliUserLocation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wamoliUserLocation);
        });

        it('should add only unique WamoliUserLocation to an array', () => {
          const wamoliUserLocationArray: IWamoliUserLocation[] = [{ id: 123 }, { id: 456 }, { id: 8665 }];
          const wamoliUserLocationCollection: IWamoliUserLocation[] = [{ id: 123 }];
          expectedResult = service.addWamoliUserLocationToCollectionIfMissing(wamoliUserLocationCollection, ...wamoliUserLocationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const wamoliUserLocation: IWamoliUserLocation = { id: 123 };
          const wamoliUserLocation2: IWamoliUserLocation = { id: 456 };
          expectedResult = service.addWamoliUserLocationToCollectionIfMissing([], wamoliUserLocation, wamoliUserLocation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wamoliUserLocation);
          expect(expectedResult).toContain(wamoliUserLocation2);
        });

        it('should accept null and undefined values', () => {
          const wamoliUserLocation: IWamoliUserLocation = { id: 123 };
          expectedResult = service.addWamoliUserLocationToCollectionIfMissing([], null, wamoliUserLocation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wamoliUserLocation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
