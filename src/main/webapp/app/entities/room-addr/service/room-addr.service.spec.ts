import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoomAddr, RoomAddr } from '../room-addr.model';

import { RoomAddrService } from './room-addr.service';

describe('Service Tests', () => {
  describe('RoomAddr Service', () => {
    let service: RoomAddrService;
    let httpMock: HttpTestingController;
    let elemDefault: IRoomAddr;
    let expectedResult: IRoomAddr | IRoomAddr[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RoomAddrService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        roomNum: 'AAAAAAA',
        unit: 'AAAAAAA',
        roomType: 'AAAAAAA',
        roomArea: 0,
        used: false,
        autoControl: false,
        longCode: 'AAAAAAA',
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

      it('should create a RoomAddr', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new RoomAddr()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RoomAddr', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            roomNum: 'BBBBBB',
            unit: 'BBBBBB',
            roomType: 'BBBBBB',
            roomArea: 1,
            used: true,
            autoControl: true,
            longCode: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a RoomAddr', () => {
        const patchObject = Object.assign(
          {
            unit: 'BBBBBB',
            used: true,
            autoControl: true,
          },
          new RoomAddr()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RoomAddr', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            roomNum: 'BBBBBB',
            unit: 'BBBBBB',
            roomType: 'BBBBBB',
            roomArea: 1,
            used: true,
            autoControl: true,
            longCode: 'BBBBBB',
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

      it('should delete a RoomAddr', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRoomAddrToCollectionIfMissing', () => {
        it('should add a RoomAddr to an empty array', () => {
          const roomAddr: IRoomAddr = { id: 123 };
          expectedResult = service.addRoomAddrToCollectionIfMissing([], roomAddr);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(roomAddr);
        });

        it('should not add a RoomAddr to an array that contains it', () => {
          const roomAddr: IRoomAddr = { id: 123 };
          const roomAddrCollection: IRoomAddr[] = [
            {
              ...roomAddr,
            },
            { id: 456 },
          ];
          expectedResult = service.addRoomAddrToCollectionIfMissing(roomAddrCollection, roomAddr);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a RoomAddr to an array that doesn't contain it", () => {
          const roomAddr: IRoomAddr = { id: 123 };
          const roomAddrCollection: IRoomAddr[] = [{ id: 456 }];
          expectedResult = service.addRoomAddrToCollectionIfMissing(roomAddrCollection, roomAddr);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(roomAddr);
        });

        it('should add only unique RoomAddr to an array', () => {
          const roomAddrArray: IRoomAddr[] = [{ id: 123 }, { id: 456 }, { id: 35610 }];
          const roomAddrCollection: IRoomAddr[] = [{ id: 123 }];
          expectedResult = service.addRoomAddrToCollectionIfMissing(roomAddrCollection, ...roomAddrArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const roomAddr: IRoomAddr = { id: 123 };
          const roomAddr2: IRoomAddr = { id: 456 };
          expectedResult = service.addRoomAddrToCollectionIfMissing([], roomAddr, roomAddr2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(roomAddr);
          expect(expectedResult).toContain(roomAddr2);
        });

        it('should accept null and undefined values', () => {
          const roomAddr: IRoomAddr = { id: 123 };
          expectedResult = service.addRoomAddrToCollectionIfMissing([], null, roomAddr, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(roomAddr);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
