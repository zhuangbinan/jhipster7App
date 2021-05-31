import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITuYaDevice, TuYaDevice } from '../tu-ya-device.model';

import { TuYaDeviceService } from './tu-ya-device.service';

describe('Service Tests', () => {
  describe('TuYaDevice Service', () => {
    let service: TuYaDeviceService;
    let httpMock: HttpTestingController;
    let elemDefault: ITuYaDevice;
    let expectedResult: ITuYaDevice | ITuYaDevice[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TuYaDeviceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        deviceName: 'AAAAAAA',
        longCode: 0,
        tyDeviceId: 'AAAAAAA',
        deviceType: 0,
        cmdCode: 'AAAAAAA',
        createTime: currentDate,
        enable: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TuYaDevice', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createTime: currentDate,
          },
          returnedFromService
        );

        service.create(new TuYaDevice()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TuYaDevice', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            deviceName: 'BBBBBB',
            longCode: 1,
            tyDeviceId: 'BBBBBB',
            deviceType: 1,
            cmdCode: 'BBBBBB',
            createTime: currentDate.format(DATE_TIME_FORMAT),
            enable: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createTime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TuYaDevice', () => {
        const patchObject = Object.assign(
          {
            deviceName: 'BBBBBB',
            longCode: 1,
          },
          new TuYaDevice()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createTime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TuYaDevice', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            deviceName: 'BBBBBB',
            longCode: 1,
            tyDeviceId: 'BBBBBB',
            deviceType: 1,
            cmdCode: 'BBBBBB',
            createTime: currentDate.format(DATE_TIME_FORMAT),
            enable: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createTime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TuYaDevice', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTuYaDeviceToCollectionIfMissing', () => {
        it('should add a TuYaDevice to an empty array', () => {
          const tuYaDevice: ITuYaDevice = { id: 123 };
          expectedResult = service.addTuYaDeviceToCollectionIfMissing([], tuYaDevice);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tuYaDevice);
        });

        it('should not add a TuYaDevice to an array that contains it', () => {
          const tuYaDevice: ITuYaDevice = { id: 123 };
          const tuYaDeviceCollection: ITuYaDevice[] = [
            {
              ...tuYaDevice,
            },
            { id: 456 },
          ];
          expectedResult = service.addTuYaDeviceToCollectionIfMissing(tuYaDeviceCollection, tuYaDevice);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TuYaDevice to an array that doesn't contain it", () => {
          const tuYaDevice: ITuYaDevice = { id: 123 };
          const tuYaDeviceCollection: ITuYaDevice[] = [{ id: 456 }];
          expectedResult = service.addTuYaDeviceToCollectionIfMissing(tuYaDeviceCollection, tuYaDevice);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tuYaDevice);
        });

        it('should add only unique TuYaDevice to an array', () => {
          const tuYaDeviceArray: ITuYaDevice[] = [{ id: 123 }, { id: 456 }, { id: 96568 }];
          const tuYaDeviceCollection: ITuYaDevice[] = [{ id: 123 }];
          expectedResult = service.addTuYaDeviceToCollectionIfMissing(tuYaDeviceCollection, ...tuYaDeviceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tuYaDevice: ITuYaDevice = { id: 123 };
          const tuYaDevice2: ITuYaDevice = { id: 456 };
          expectedResult = service.addTuYaDeviceToCollectionIfMissing([], tuYaDevice, tuYaDevice2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tuYaDevice);
          expect(expectedResult).toContain(tuYaDevice2);
        });

        it('should accept null and undefined values', () => {
          const tuYaDevice: ITuYaDevice = { id: 123 };
          expectedResult = service.addTuYaDeviceToCollectionIfMissing([], null, tuYaDevice, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tuYaDevice);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
