jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITest01, Test01 } from '../test-01.model';
import { Test01Service } from '../service/test-01.service';

import { Test01RoutingResolveService } from './test-01-routing-resolve.service';

describe('Service Tests', () => {
  describe('Test01 routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: Test01RoutingResolveService;
    let service: Test01Service;
    let resultTest01: ITest01 | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(Test01RoutingResolveService);
      service = TestBed.inject(Test01Service);
      resultTest01 = undefined;
    });

    describe('resolve', () => {
      it('should return ITest01 returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTest01 = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTest01).toEqual({ id: 123 });
      });

      it('should return new ITest01 if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTest01 = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTest01).toEqual(new Test01());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTest01 = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTest01).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
