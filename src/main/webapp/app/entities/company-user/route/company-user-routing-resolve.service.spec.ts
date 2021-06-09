jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICompanyUser, CompanyUser } from '../company-user.model';
import { CompanyUserService } from '../service/company-user.service';

import { CompanyUserRoutingResolveService } from './company-user-routing-resolve.service';

describe('Service Tests', () => {
  describe('CompanyUser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CompanyUserRoutingResolveService;
    let service: CompanyUserService;
    let resultCompanyUser: ICompanyUser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CompanyUserRoutingResolveService);
      service = TestBed.inject(CompanyUserService);
      resultCompanyUser = undefined;
    });

    describe('resolve', () => {
      it('should return ICompanyUser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompanyUser).toEqual({ id: 123 });
      });

      it('should return new ICompanyUser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyUser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCompanyUser).toEqual(new CompanyUser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompanyUser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
