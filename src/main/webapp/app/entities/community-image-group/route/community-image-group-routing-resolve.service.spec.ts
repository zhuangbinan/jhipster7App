jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommunityImageGroup, CommunityImageGroup } from '../community-image-group.model';
import { CommunityImageGroupService } from '../service/community-image-group.service';

import { CommunityImageGroupRoutingResolveService } from './community-image-group-routing-resolve.service';

describe('Service Tests', () => {
  describe('CommunityImageGroup routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CommunityImageGroupRoutingResolveService;
    let service: CommunityImageGroupService;
    let resultCommunityImageGroup: ICommunityImageGroup | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CommunityImageGroupRoutingResolveService);
      service = TestBed.inject(CommunityImageGroupService);
      resultCommunityImageGroup = undefined;
    });

    describe('resolve', () => {
      it('should return ICommunityImageGroup returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityImageGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunityImageGroup).toEqual({ id: 123 });
      });

      it('should return new ICommunityImageGroup if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityImageGroup = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCommunityImageGroup).toEqual(new CommunityImageGroup());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCommunityImageGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCommunityImageGroup).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
