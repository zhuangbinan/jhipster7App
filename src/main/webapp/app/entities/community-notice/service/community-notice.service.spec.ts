import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICommunityNotice, CommunityNotice } from '../community-notice.model';

import { CommunityNoticeService } from './community-notice.service';

describe('Service Tests', () => {
  describe('CommunityNotice Service', () => {
    let service: CommunityNoticeService;
    let httpMock: HttpTestingController;
    let elemDefault: ICommunityNotice;
    let expectedResult: ICommunityNotice | ICommunityNotice[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CommunityNoticeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        img1ContentType: 'image/png',
        img1: 'AAAAAAA',
        img1Title: 'AAAAAAA',
        content1: 'AAAAAAA',
        img2ContentType: 'image/png',
        img2: 'AAAAAAA',
        img2Title: 'AAAAAAA',
        content2: 'AAAAAAA',
        img3ContentType: 'image/png',
        img3: 'AAAAAAA',
        img3Title: 'AAAAAAA',
        content3: 'AAAAAAA',
        img4ContentType: 'image/png',
        img4: 'AAAAAAA',
        img4Title: 'AAAAAAA',
        content4: 'AAAAAAA',
        img5ContentType: 'image/png',
        img5: 'AAAAAAA',
        img5Title: 'AAAAAAA',
        content5: 'AAAAAAA',
        tail: 'AAAAAAA',
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

      it('should create a CommunityNotice', () => {
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

        service.create(new CommunityNotice()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CommunityNotice', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            img1: 'BBBBBB',
            img1Title: 'BBBBBB',
            content1: 'BBBBBB',
            img2: 'BBBBBB',
            img2Title: 'BBBBBB',
            content2: 'BBBBBB',
            img3: 'BBBBBB',
            img3Title: 'BBBBBB',
            content3: 'BBBBBB',
            img4: 'BBBBBB',
            img4Title: 'BBBBBB',
            content4: 'BBBBBB',
            img5: 'BBBBBB',
            img5Title: 'BBBBBB',
            content5: 'BBBBBB',
            tail: 'BBBBBB',
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

      it('should partial update a CommunityNotice', () => {
        const patchObject = Object.assign(
          {
            img2Title: 'BBBBBB',
            img3: 'BBBBBB',
            img3Title: 'BBBBBB',
            content3: 'BBBBBB',
            img4Title: 'BBBBBB',
            img5: 'BBBBBB',
            orderNum: 1,
          },
          new CommunityNotice()
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

      it('should return a list of CommunityNotice', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            img1: 'BBBBBB',
            img1Title: 'BBBBBB',
            content1: 'BBBBBB',
            img2: 'BBBBBB',
            img2Title: 'BBBBBB',
            content2: 'BBBBBB',
            img3: 'BBBBBB',
            img3Title: 'BBBBBB',
            content3: 'BBBBBB',
            img4: 'BBBBBB',
            img4Title: 'BBBBBB',
            content4: 'BBBBBB',
            img5: 'BBBBBB',
            img5Title: 'BBBBBB',
            content5: 'BBBBBB',
            tail: 'BBBBBB',
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

      it('should delete a CommunityNotice', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCommunityNoticeToCollectionIfMissing', () => {
        it('should add a CommunityNotice to an empty array', () => {
          const communityNotice: ICommunityNotice = { id: 123 };
          expectedResult = service.addCommunityNoticeToCollectionIfMissing([], communityNotice);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communityNotice);
        });

        it('should not add a CommunityNotice to an array that contains it', () => {
          const communityNotice: ICommunityNotice = { id: 123 };
          const communityNoticeCollection: ICommunityNotice[] = [
            {
              ...communityNotice,
            },
            { id: 456 },
          ];
          expectedResult = service.addCommunityNoticeToCollectionIfMissing(communityNoticeCollection, communityNotice);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CommunityNotice to an array that doesn't contain it", () => {
          const communityNotice: ICommunityNotice = { id: 123 };
          const communityNoticeCollection: ICommunityNotice[] = [{ id: 456 }];
          expectedResult = service.addCommunityNoticeToCollectionIfMissing(communityNoticeCollection, communityNotice);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communityNotice);
        });

        it('should add only unique CommunityNotice to an array', () => {
          const communityNoticeArray: ICommunityNotice[] = [{ id: 123 }, { id: 456 }, { id: 97238 }];
          const communityNoticeCollection: ICommunityNotice[] = [{ id: 123 }];
          expectedResult = service.addCommunityNoticeToCollectionIfMissing(communityNoticeCollection, ...communityNoticeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const communityNotice: ICommunityNotice = { id: 123 };
          const communityNotice2: ICommunityNotice = { id: 456 };
          expectedResult = service.addCommunityNoticeToCollectionIfMissing([], communityNotice, communityNotice2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(communityNotice);
          expect(expectedResult).toContain(communityNotice2);
        });

        it('should accept null and undefined values', () => {
          const communityNotice: ICommunityNotice = { id: 123 };
          expectedResult = service.addCommunityNoticeToCollectionIfMissing([], null, communityNotice, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(communityNotice);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
