import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICommunityImages, CommunityImages } from '../community-images.model';

import { CommunityImagesService } from './community-images.service';

describe('Service Tests', () => {
  describe('CommunityImages Service', () => {
    let service: CommunityImagesService;
    let httpMock: HttpTestingController;
    let elemDefault: ICommunityImages;
    let expectedResult: ICommunityImages | ICommunityImages[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CommunityImagesService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        imgContentContentType: 'image/png',
        imgContent: 'AAAAAAA',
        imgTitle: 'AAAAAAA',
        imgDesc: 'AAAAAAA',
        orderNum: 0,
        lastModifyDate: currentDate,
        lastModifyBy: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CommunityImages', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastModifyDate: currentDate,
          },
          returnedFromService
        );

        service.create(new CommunityImages()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CommunityImages', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            imgContent: 'BBBBBB',
            imgTitle: 'BBBBBB',
            imgDesc: 'BBBBBB',
            orderNum: 1,
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyBy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastModifyDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CommunityImages', () => {
        const patchObject = Object.assign(
          {
            imgContent: 'BBBBBB',
            imgTitle: 'BBBBBB',
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyBy: 'BBBBBB',
          },
          new CommunityImages()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            lastModifyDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CommunityImages', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            imgContent: 'BBBBBB',
            imgTitle: 'BBBBBB',
            imgDesc: 'BBBBBB',
            orderNum: 1,
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyBy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should delete a CommunityImages', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCommunityImagesToCollectionIfMissing', () => {
        it('should add a CommunityImages to an empty array', () => {
          const communityImages: ICommunityImages = { id: 123 };
          expectedResult = service.addCommunityImagesToCollectionIfMissing([], communityImages);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communityImages);
        });

        it('should not add a CommunityImages to an array that contains it', () => {
          const communityImages: ICommunityImages = { id: 123 };
          const communityImagesCollection: ICommunityImages[] = [
            {
              ...communityImages,
            },
            { id: 456 },
          ];
          expectedResult = service.addCommunityImagesToCollectionIfMissing(communityImagesCollection, communityImages);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CommunityImages to an array that doesn't contain it", () => {
          const communityImages: ICommunityImages = { id: 123 };
          const communityImagesCollection: ICommunityImages[] = [{ id: 456 }];
          expectedResult = service.addCommunityImagesToCollectionIfMissing(communityImagesCollection, communityImages);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communityImages);
        });

        it('should add only unique CommunityImages to an array', () => {
          const communityImagesArray: ICommunityImages[] = [{ id: 123 }, { id: 456 }, { id: 33722 }];
          const communityImagesCollection: ICommunityImages[] = [{ id: 123 }];
          expectedResult = service.addCommunityImagesToCollectionIfMissing(communityImagesCollection, ...communityImagesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const communityImages: ICommunityImages = { id: 123 };
          const communityImages2: ICommunityImages = { id: 456 };
          expectedResult = service.addCommunityImagesToCollectionIfMissing([], communityImages, communityImages2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communityImages);
          expect(expectedResult).toContain(communityImages2);
        });

        it('should accept null and undefined values', () => {
          const communityImages: ICommunityImages = { id: 123 };
          expectedResult = service.addCommunityImagesToCollectionIfMissing([], null, communityImages, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communityImages);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
