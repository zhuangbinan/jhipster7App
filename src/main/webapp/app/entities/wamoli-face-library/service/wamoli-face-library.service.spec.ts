import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWamoliFaceLibrary, WamoliFaceLibrary } from '../wamoli-face-library.model';

import { WamoliFaceLibraryService } from './wamoli-face-library.service';

describe('Service Tests', () => {
  describe('WamoliFaceLibrary Service', () => {
    let service: WamoliFaceLibraryService;
    let httpMock: HttpTestingController;
    let elemDefault: IWamoliFaceLibrary;
    let expectedResult: IWamoliFaceLibrary | IWamoliFaceLibrary[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WamoliFaceLibraryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        content: 'AAAAAAA',
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

      it('should create a WamoliFaceLibrary', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new WamoliFaceLibrary()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WamoliFaceLibrary', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            content: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WamoliFaceLibrary', () => {
        const patchObject = Object.assign(
          {
            content: 'BBBBBB',
          },
          new WamoliFaceLibrary()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WamoliFaceLibrary', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            content: 'BBBBBB',
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

      it('should delete a WamoliFaceLibrary', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWamoliFaceLibraryToCollectionIfMissing', () => {
        it('should add a WamoliFaceLibrary to an empty array', () => {
          const wamoliFaceLibrary: IWamoliFaceLibrary = { id: 123 };
          expectedResult = service.addWamoliFaceLibraryToCollectionIfMissing([], wamoliFaceLibrary);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wamoliFaceLibrary);
        });

        it('should not add a WamoliFaceLibrary to an array that contains it', () => {
          const wamoliFaceLibrary: IWamoliFaceLibrary = { id: 123 };
          const wamoliFaceLibraryCollection: IWamoliFaceLibrary[] = [
            {
              ...wamoliFaceLibrary,
            },
            { id: 456 },
          ];
          expectedResult = service.addWamoliFaceLibraryToCollectionIfMissing(wamoliFaceLibraryCollection, wamoliFaceLibrary);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WamoliFaceLibrary to an array that doesn't contain it", () => {
          const wamoliFaceLibrary: IWamoliFaceLibrary = { id: 123 };
          const wamoliFaceLibraryCollection: IWamoliFaceLibrary[] = [{ id: 456 }];
          expectedResult = service.addWamoliFaceLibraryToCollectionIfMissing(wamoliFaceLibraryCollection, wamoliFaceLibrary);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wamoliFaceLibrary);
        });

        it('should add only unique WamoliFaceLibrary to an array', () => {
          const wamoliFaceLibraryArray: IWamoliFaceLibrary[] = [{ id: 123 }, { id: 456 }, { id: 64088 }];
          const wamoliFaceLibraryCollection: IWamoliFaceLibrary[] = [{ id: 123 }];
          expectedResult = service.addWamoliFaceLibraryToCollectionIfMissing(wamoliFaceLibraryCollection, ...wamoliFaceLibraryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const wamoliFaceLibrary: IWamoliFaceLibrary = { id: 123 };
          const wamoliFaceLibrary2: IWamoliFaceLibrary = { id: 456 };
          expectedResult = service.addWamoliFaceLibraryToCollectionIfMissing([], wamoliFaceLibrary, wamoliFaceLibrary2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(wamoliFaceLibrary);
          expect(expectedResult).toContain(wamoliFaceLibrary2);
        });

        it('should accept null and undefined values', () => {
          const wamoliFaceLibrary: IWamoliFaceLibrary = { id: 123 };
          expectedResult = service.addWamoliFaceLibraryToCollectionIfMissing([], null, wamoliFaceLibrary, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(wamoliFaceLibrary);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
