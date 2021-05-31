import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHomelandStation, HomelandStation } from '../homeland-station.model';

import { HomelandStationService } from './homeland-station.service';

describe('Service Tests', () => {
  describe('HomelandStation Service', () => {
    let service: HomelandStationService;
    let httpMock: HttpTestingController;
    let elemDefault: IHomelandStation;
    let expectedResult: IHomelandStation | IHomelandStation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(HomelandStationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        longCode: 'AAAAAAA',
        address: 'AAAAAAA',
        livingPopulation: 0,
        buildingCount: 0,
        entranceCount: 0,
        logoContentType: 'image/png',
        logo: 'AAAAAAA',
        longitude: 'AAAAAAA',
        latitude: 'AAAAAAA',
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

      it('should create a HomelandStation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new HomelandStation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a HomelandStation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            longCode: 'BBBBBB',
            address: 'BBBBBB',
            livingPopulation: 1,
            buildingCount: 1,
            entranceCount: 1,
            logo: 'BBBBBB',
            longitude: 'BBBBBB',
            latitude: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a HomelandStation', () => {
        const patchObject = Object.assign(
          {
            address: 'BBBBBB',
            entranceCount: 1,
          },
          new HomelandStation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of HomelandStation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            longCode: 'BBBBBB',
            address: 'BBBBBB',
            livingPopulation: 1,
            buildingCount: 1,
            entranceCount: 1,
            logo: 'BBBBBB',
            longitude: 'BBBBBB',
            latitude: 'BBBBBB',
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

      it('should delete a HomelandStation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addHomelandStationToCollectionIfMissing', () => {
        it('should add a HomelandStation to an empty array', () => {
          const homelandStation: IHomelandStation = { id: 123 };
          expectedResult = service.addHomelandStationToCollectionIfMissing([], homelandStation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(homelandStation);
        });

        it('should not add a HomelandStation to an array that contains it', () => {
          const homelandStation: IHomelandStation = { id: 123 };
          const homelandStationCollection: IHomelandStation[] = [
            {
              ...homelandStation,
            },
            { id: 456 },
          ];
          expectedResult = service.addHomelandStationToCollectionIfMissing(homelandStationCollection, homelandStation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a HomelandStation to an array that doesn't contain it", () => {
          const homelandStation: IHomelandStation = { id: 123 };
          const homelandStationCollection: IHomelandStation[] = [{ id: 456 }];
          expectedResult = service.addHomelandStationToCollectionIfMissing(homelandStationCollection, homelandStation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(homelandStation);
        });

        it('should add only unique HomelandStation to an array', () => {
          const homelandStationArray: IHomelandStation[] = [{ id: 123 }, { id: 456 }, { id: 59028 }];
          const homelandStationCollection: IHomelandStation[] = [{ id: 123 }];
          expectedResult = service.addHomelandStationToCollectionIfMissing(homelandStationCollection, ...homelandStationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const homelandStation: IHomelandStation = { id: 123 };
          const homelandStation2: IHomelandStation = { id: 456 };
          expectedResult = service.addHomelandStationToCollectionIfMissing([], homelandStation, homelandStation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(homelandStation);
          expect(expectedResult).toContain(homelandStation2);
        });

        it('should accept null and undefined values', () => {
          const homelandStation: IHomelandStation = { id: 123 };
          expectedResult = service.addHomelandStationToCollectionIfMissing([], null, homelandStation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(homelandStation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
