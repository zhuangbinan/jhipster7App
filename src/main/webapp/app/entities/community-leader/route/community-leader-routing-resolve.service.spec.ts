jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommunityLeader, CommunityLeader } from '../community-leader.model';
import { CommunityLeaderService } from '../service/community-leader.service';

import { CommunityLeaderRoutingResolveService } from './community-leader-routing-resolve.service';

describe('Service Tests', () => {
  describe('CommunityLeader routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommunityLeaderRoutingResolveService;
    let service: CommunityLeaderService;
    let resultCommunityLeader: ICommunityLeader | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommunityLeaderRoutingResolveService);
      service = TestBed.inject(CommunityLeaderService);
      resultCommunityLeader = undefined;
    });

    describe('resolve', () => {
      it('should return ICommunityLeader returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityLeader = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunityLeader).toEqual({ id: 123 });
      });

      it('should return new ICommunityLeader if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityLeader = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCommunityLeader).toEqual(new CommunityLeader());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityLeader = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunityLeader).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
