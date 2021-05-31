import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { CmdType } from 'app/entities/enumerations/cmd-type.model';
import { ITuYaCmd, TuYaCmd } from '../tu-ya-cmd.model';

import { TuYaCmdService } from './tu-ya-cmd.service';

describe('Service Tests', () => {
  describe('TuYaCmd Service', () => {
    let service: TuYaCmdService;
    let httpMock: HttpTestingController;
    let elemDefault: ITuYaCmd;
    let expectedResult: ITuYaCmd | ITuYaCmd[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TuYaCmdService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        cmdName: 'AAAAAAA',
        cmdCode: 'AAAAAAA',
        value: false,
        cmdType: CmdType.ON,
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

      it('should create a TuYaCmd', () => {
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

        service.create(new TuYaCmd()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TuYaCmd', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cmdName: 'BBBBBB',
            cmdCode: 'BBBBBB',
            value: true,
            cmdType: 'BBBBBB',
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

      it('should partial update a TuYaCmd', () => {
        const patchObject = Object.assign(
          {
            enable: true,
          },
          new TuYaCmd()
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

      it('should return a list of TuYaCmd', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cmdName: 'BBBBBB',
            cmdCode: 'BBBBBB',
            value: true,
            cmdType: 'BBBBBB',
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

      it('should delete a TuYaCmd', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTuYaCmdToCollectionIfMissing', () => {
        it('should add a TuYaCmd to an empty array', () => {
          const tuYaCmd: ITuYaCmd = { id: 123 };
          expectedResult = service.addTuYaCmdToCollectionIfMissing([], tuYaCmd);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tuYaCmd);
        });

        it('should not add a TuYaCmd to an array that contains it', () => {
          const tuYaCmd: ITuYaCmd = { id: 123 };
          const tuYaCmdCollection: ITuYaCmd[] = [
            {
              ...tuYaCmd,
            },
            { id: 456 },
          ];
          expectedResult = service.addTuYaCmdToCollectionIfMissing(tuYaCmdCollection, tuYaCmd);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TuYaCmd to an array that doesn't contain it", () => {
          const tuYaCmd: ITuYaCmd = { id: 123 };
          const tuYaCmdCollection: ITuYaCmd[] = [{ id: 456 }];
          expectedResult = service.addTuYaCmdToCollectionIfMissing(tuYaCmdCollection, tuYaCmd);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tuYaCmd);
        });

        it('should add only unique TuYaCmd to an array', () => {
          const tuYaCmdArray: ITuYaCmd[] = [{ id: 123 }, { id: 456 }, { id: 34136 }];
          const tuYaCmdCollection: ITuYaCmd[] = [{ id: 123 }];
          expectedResult = service.addTuYaCmdToCollectionIfMissing(tuYaCmdCollection, ...tuYaCmdArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tuYaCmd: ITuYaCmd = { id: 123 };
          const tuYaCmd2: ITuYaCmd = { id: 456 };
          expectedResult = service.addTuYaCmdToCollectionIfMissing([], tuYaCmd, tuYaCmd2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tuYaCmd);
          expect(expectedResult).toContain(tuYaCmd2);
        });

        it('should accept null and undefined values', () => {
          const tuYaCmd: ITuYaCmd = { id: 123 };
          expectedResult = service.addTuYaCmdToCollectionIfMissing([], null, tuYaCmd, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tuYaCmd);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
