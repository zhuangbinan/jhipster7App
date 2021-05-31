import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICommunityLeader, CommunityLeader } from '../community-leader.model';

import { CommunityLeaderService } from './community-leader.service';

describe('Service Tests', () => {
  describe('CommunityLeader Service', () => {
    let service: CommunityLeaderService;
    let httpMock: HttpTestingController;
    let elemDefault: ICommunityLeader;
    let expectedResult: ICommunityLeader | ICommunityLeader[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CommunityLeaderService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        avatarContentType: 'image/png',
        avatar: 'AAAAAAA',
        realName: 'AAAAAAA',
        tel: 'AAAAAAA',
        jobTitle: 'AAAAAAA',
        jobDesc: 'AAAAAAA',
        jobCareerDesc: 'AAAAAAA',
        enable: false,
        delFlag: false,
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

      it('should create a CommunityLeader', () => {
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

        service.create(new CommunityLeader()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CommunityLeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            avatar: 'BBBBBB',
            realName: 'BBBBBB',
            tel: 'BBBBBB',
            jobTitle: 'BBBBBB',
            jobDesc: 'BBBBBB',
            jobCareerDesc: 'BBBBBB',
            enable: true,
            delFlag: true,
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

      it('should partial update a CommunityLeader', () => {
        const patchObject = Object.assign(
          {
            avatar: 'BBBBBB',
            realName: 'BBBBBB',
            jobDesc: 'BBBBBB',
            enable: true,
            delFlag: true,
            orderNum: 1,
            lastModifyDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifyBy: 'BBBBBB',
          },
          new CommunityLeader()
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

      it('should return a list of CommunityLeader', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            avatar: 'BBBBBB',
            realName: 'BBBBBB',
            tel: 'BBBBBB',
            jobTitle: 'BBBBBB',
            jobDesc: 'BBBBBB',
            jobCareerDesc: 'BBBBBB',
            enable: true,
            delFlag: true,
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

      it('should delete a CommunityLeader', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCommunityLeaderToCollectionIfMissing', () => {
        it('should add a CommunityLeader to an empty array', () => {
          const communityLeader: ICommunityLeader = { id: 123 };
          expectedResult = service.addCommunityLeaderToCollectionIfMissing([], communityLeader);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communityLeader);
        });

        it('should not add a CommunityLeader to an array that contains it', () => {
          const communityLeader: ICommunityLeader = { id: 123 };
          const communityLeaderCollection: ICommunityLeader[] = [
            {
              ...communityLeader,
            },
            { id: 456 },
          ];
          expectedResult = service.addCommunityLeaderToCollectionIfMissing(communityLeaderCollection, communityLeader);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CommunityLeader to an array that doesn't contain it", () => {
          const communityLeader: ICommunityLeader = { id: 123 };
          const communityLeaderCollection: ICommunityLeader[] = [{ id: 456 }];
          expectedResult = service.addCommunityLeaderToCollectionIfMissing(communityLeaderCollection, communityLeader);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communityLeader);
        });

        it('should add only unique CommunityLeader to an array', () => {
          const communityLeaderArray: ICommunityLeader[] = [{ id: 123 }, { id: 456 }, { id: 69927 }];
          const communityLeaderCollection: ICommunityLeader[] = [{ id: 123 }];
          expectedResult = service.addCommunityLeaderToCollectionIfMissing(communityLeaderCollection, ...communityLeaderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const communityLeader: ICommunityLeader = { id: 123 };
          const communityLeader2: ICommunityLeader = { id: 456 };
          expectedResult = service.addCommunityLeaderToCollectionIfMissing([], communityLeader, communityLeader2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communityLeader);
          expect(expectedResult).toContain(communityLeader2);
        });

        it('should accept null and undefined values', () => {
          const communityLeader: ICommunityLeader = { id: 123 };
          expectedResult = service.addCommunityLeaderToCollectionIfMissing([], null, communityLeader, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communityLeader);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
