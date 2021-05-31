jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IHomelandStation, HomelandStation } from '../homeland-station.model';
import { HomelandStationService } from '../service/homeland-station.service';

import { HomelandStationRoutingResolveService } from './homeland-station-routing-resolve.service';

describe('Service Tests', () => {
  describe('HomelandStation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: HomelandStationRoutingResolveService;
    let service: HomelandStationService;
    let resultHomelandStation: IHomelandStation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(HomelandStationRoutingResolveService);
      service = TestBed.inject(HomelandStationService);
      resultHomelandStation = undefined;
    });

    describe('resolve', () => {
      it('should return IHomelandStation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHomelandStation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultHomelandStation).toEqual({ id: 123 });
      });

      it('should return new IHomelandStation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHomelandStation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultHomelandStation).toEqual(new HomelandStation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultHomelandStation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultHomelandStation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
