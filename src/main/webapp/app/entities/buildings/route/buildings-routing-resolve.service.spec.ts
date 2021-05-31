jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBuildings, Buildings } from '../buildings.model';
import { BuildingsService } from '../service/buildings.service';

import { BuildingsRoutingResolveService } from './buildings-routing-resolve.service';

describe('Service Tests', () => {
  describe('Buildings routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BuildingsRoutingResolveService;
    let service: BuildingsService;
    let resultBuildings: IBuildings | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BuildingsRoutingResolveService);
      service = TestBed.inject(BuildingsService);
      resultBuildings = undefined;
    });

    describe('resolve', () => {
      it('should return IBuildings returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBuildings = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBuildings).toEqual({ id: 123 });
      });

      it('should return new IBuildings if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBuildings = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBuildings).toEqual(new Buildings());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBuildings = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBuildings).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
