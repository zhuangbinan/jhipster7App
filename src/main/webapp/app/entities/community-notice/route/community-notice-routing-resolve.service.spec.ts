jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommunityNotice, CommunityNotice } from '../community-notice.model';
import { CommunityNoticeService } from '../service/community-notice.service';

import { CommunityNoticeRoutingResolveService } from './community-notice-routing-resolve.service';

describe('Service Tests', () => {
  describe('CommunityNotice routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommunityNoticeRoutingResolveService;
    let service: CommunityNoticeService;
    let resultCommunityNotice: ICommunityNotice | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommunityNoticeRoutingResolveService);
      service = TestBed.inject(CommunityNoticeService);
      resultCommunityNotice = undefined;
    });

    describe('resolve', () => {
      it('should return ICommunityNotice returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityNotice = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunityNotice).toEqual({ id: 123 });
      });

      it('should return new ICommunityNotice if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityNotice = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCommunityNotice).toEqual(new CommunityNotice());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityNotice = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunityNotice).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
