jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWamoliUser, WamoliUser } from '../wamoli-user.model';
import { WamoliUserService } from '../service/wamoli-user.service';

import { WamoliUserRoutingResolveService } from './wamoli-user-routing-resolve.service';

describe('Service Tests', () => {
  describe('WamoliUser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: WamoliUserRoutingResolveService;
    let service: WamoliUserService;
    let resultWamoliUser: IWamoliUser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(WamoliUserRoutingResolveService);
      service = TestBed.inject(WamoliUserService);
      resultWamoliUser = undefined;
    });

    describe('resolve', () => {
      it('should return IWamoliUser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWamoliUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWamoliUser).toEqual({ id: 123 });
      });

      it('should return new IWamoliUser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWamoliUser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultWamoliUser).toEqual(new WamoliUser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWamoliUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWamoliUser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
