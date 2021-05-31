import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVisitor, Visitor } from '../visitor.model';

import { VisitorService } from './visitor.service';

describe('Service Tests', () => {
  describe('Visitor Service', () => {
    let service: VisitorService;
    let httpMock: HttpTestingController;
    let elemDefault: IVisitor;
    let expectedResult: IVisitor | IVisitor[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VisitorService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        phoneNum: 'AAAAAAA',
        carPlateNum: 'AAAAAAA',
        startTime: currentDate,
        endTime: currentDate,
        passwd: 'AAAAAAA',
        faceImageContentType: 'image/png',
        faceImage: 'AAAAAAA',
        whichEntrance: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startTime: currentDate.format(DATE_TIME_FORMAT),
            endTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Visitor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startTime: currentDate.format(DATE_TIME_FORMAT),
            endTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startTime: currentDate,
            endTime: currentDate,
          },
          returnedFromService
        );

        service.create(new Visitor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Visitor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            phoneNum: 'BBBBBB',
            carPlateNum: 'BBBBBB',
            startTime: currentDate.format(DATE_TIME_FORMAT),
            endTime: currentDate.format(DATE_TIME_FORMAT),
            passwd: 'BBBBBB',
            faceImage: 'BBBBBB',
            whichEntrance: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startTime: currentDate,
            endTime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Visitor', () => {
        const patchObject = Object.assign(
          {
            phoneNum: 'BBBBBB',
            carPlateNum: 'BBBBBB',
            faceImage: 'BBBBBB',
            whichEntrance: 'BBBBBB',
          },
          new Visitor()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startTime: currentDate,
            endTime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Visitor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            phoneNum: 'BBBBBB',
            carPlateNum: 'BBBBBB',
            startTime: currentDate.format(DATE_TIME_FORMAT),
            endTime: currentDate.format(DATE_TIME_FORMAT),
            passwd: 'BBBBBB',
            faceImage: 'BBBBBB',
            whichEntrance: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startTime: currentDate,
            endTime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Visitor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVisitorToCollectionIfMissing', () => {
        it('should add a Visitor to an empty array', () => {
          const visitor: IVisitor = { id: 123 };
          expectedResult = service.addVisitorToCollectionIfMissing([], visitor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(visitor);
        });

        it('should not add a Visitor to an array that contains it', () => {
          const visitor: IVisitor = { id: 123 };
          const visitorCollection: IVisitor[] = [
            {
              ...visitor,
            },
            { id: 456 },
          ];
          expectedResult = service.addVisitorToCollectionIfMissing(visitorCollection, visitor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Visitor to an array that doesn't contain it", () => {
          const visitor: IVisitor = { id: 123 };
          const visitorCollection: IVisitor[] = [{ id: 456 }];
          expectedResult = service.addVisitorToCollectionIfMissing(visitorCollection, visitor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(visitor);
        });

        it('should add only unique Visitor to an array', () => {
          const visitorArray: IVisitor[] = [{ id: 123 }, { id: 456 }, { id: 36571 }];
          const visitorCollection: IVisitor[] = [{ id: 123 }];
          expectedResult = service.addVisitorToCollectionIfMissing(visitorCollection, ...visitorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const visitor: IVisitor = { id: 123 };
          const visitor2: IVisitor = { id: 456 };
          expectedResult = service.addVisitorToCollectionIfMissing([], visitor, visitor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(visitor);
          expect(expectedResult).toContain(visitor2);
        });

        it('should accept null and undefined values', () => {
          const visitor: IVisitor = { id: 123 };
          expectedResult = service.addVisitorToCollectionIfMissing([], null, visitor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(visitor);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
