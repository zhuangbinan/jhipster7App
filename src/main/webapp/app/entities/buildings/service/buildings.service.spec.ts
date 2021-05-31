import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBuildings, Buildings } from '../buildings.model';

import { BuildingsService } from './buildings.service';

describe('Service Tests', () => {
  describe('Buildings Service', () => {
    let service: BuildingsService;
    let httpMock: HttpTestingController;
    let elemDefault: IBuildings;
    let expectedResult: IBuildings | IBuildings[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BuildingsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        longCode: 'AAAAAAA',
        floorCount: 0,
        unites: 0,
        longitude: 'AAAAAAA',
        latitude: 'AAAAAAA',
        enable: false,
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

      it('should create a Buildings', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Buildings()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Buildings', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            longCode: 'BBBBBB',
            floorCount: 1,
            unites: 1,
            longitude: 'BBBBBB',
            latitude: 'BBBBBB',
            enable: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Buildings', () => {
        const patchObject = Object.assign(
          {
            floorCount: 1,
            longitude: 'BBBBBB',
            enable: true,
          },
          new Buildings()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Buildings', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            longCode: 'BBBBBB',
            floorCount: 1,
            unites: 1,
            longitude: 'BBBBBB',
            latitude: 'BBBBBB',
            enable: true,
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

      it('should delete a Buildings', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBuildingsToCollectionIfMissing', () => {
        it('should add a Buildings to an empty array', () => {
          const buildings: IBuildings = { id: 123 };
          expectedResult = service.addBuildingsToCollectionIfMissing([], buildings);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(buildings);
        });

        it('should not add a Buildings to an array that contains it', () => {
          const buildings: IBuildings = { id: 123 };
          const buildingsCollection: IBuildings[] = [
            {
              ...buildings,
            },
            { id: 456 },
          ];
          expectedResult = service.addBuildingsToCollectionIfMissing(buildingsCollection, buildings);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Buildings to an array that doesn't contain it", () => {
          const buildings: IBuildings = { id: 123 };
          const buildingsCollection: IBuildings[] = [{ id: 456 }];
          expectedResult = service.addBuildingsToCollectionIfMissing(buildingsCollection, buildings);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(buildings);
        });

        it('should add only unique Buildings to an array', () => {
          const buildingsArray: IBuildings[] = [{ id: 123 }, { id: 456 }, { id: 47517 }];
          const buildingsCollection: IBuildings[] = [{ id: 123 }];
          expectedResult = service.addBuildingsToCollectionIfMissing(buildingsCollection, ...buildingsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const buildings: IBuildings = { id: 123 };
          const buildings2: IBuildings = { id: 456 };
          expectedResult = service.addBuildingsToCollectionIfMissing([], buildings, buildings2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(buildings);
          expect(expectedResult).toContain(buildings2);
        });

        it('should accept null and undefined values', () => {
          const buildings: IBuildings = { id: 123 };
          expectedResult = service.addBuildingsToCollectionIfMissing([], null, buildings, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(buildings);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
