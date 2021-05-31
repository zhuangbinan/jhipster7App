import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICommunityImageGroup, CommunityImageGroup } from '../community-image-group.model';

import { CommunityImageGroupService } from './community-image-group.service';

describe('Service Tests', () => {
  describe('CommunityImageGroup Service', () => {
    let service: CommunityImageGroupService;
    let httpMock: HttpTestingController;
    let elemDefault: ICommunityImageGroup;
    let expectedResult: ICommunityImageGroup | ICommunityImageGroup[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CommunityImageGroupService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        imgGroupName: 'AAAAAAA',
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

      it('should create a CommunityImageGroup', () => {
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

        service.create(new CommunityImageGroup()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CommunityImageGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            imgGroupName: 'BBBBBB',
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

      it('should partial update a CommunityImageGroup', () => {
        const patchObject = Object.assign(
          {
            orderNum: 1,
          },
          new CommunityImageGroup()
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

      it('should return a list of CommunityImageGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            imgGroupName: 'BBBBBB',
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

      it('should delete a CommunityImageGroup', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCommunityImageGroupToCollectionIfMissing', () => {
        it('should add a CommunityImageGroup to an empty array', () => {
          const communityImageGroup: ICommunityImageGroup = { id: 123 };
          expectedResult = service.addCommunityImageGroupToCollectionIfMissing([], communityImageGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communityImageGroup);
        });

        it('should not add a CommunityImageGroup to an array that contains it', () => {
          const communityImageGroup: ICommunityImageGroup = { id: 123 };
          const communityImageGroupCollection: ICommunityImageGroup[] = [
            {
              ...communityImageGroup,
            },
            { id: 456 },
          ];
          expectedResult = service.addCommunityImageGroupToCollectionIfMissing(communityImageGroupCollection, communityImageGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CommunityImageGroup to an array that doesn't contain it", () => {
          const communityImageGroup: ICommunityImageGroup = { id: 123 };
          const communityImageGroupCollection: ICommunityImageGroup[] = [{ id: 456 }];
          expectedResult = service.addCommunityImageGroupToCollectionIfMissing(communityImageGroupCollection, communityImageGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communityImageGroup);
        });

        it('should add only unique CommunityImageGroup to an array', () => {
          const communityImageGroupArray: ICommunityImageGroup[] = [{ id: 123 }, { id: 456 }, { id: 59463 }];
          const communityImageGroupCollection: ICommunityImageGroup[] = [{ id: 123 }];
          expectedResult = service.addCommunityImageGroupToCollectionIfMissing(communityImageGroupCollection, ...communityImageGroupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const communityImageGroup: ICommunityImageGroup = { id: 123 };
          const communityImageGroup2: ICommunityImageGroup = { id: 456 };
          expectedResult = service.addCommunityImageGroupToCollectionIfMissing([], communityImageGroup, communityImageGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communityImageGroup);
          expect(expectedResult).toContain(communityImageGroup2);
        });

        it('should accept null and undefined values', () => {
          const communityImageGroup: ICommunityImageGroup = { id: 123 };
          expectedResult = service.addCommunityImageGroupToCollectionIfMissing([], null, communityImageGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communityImageGroup);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
