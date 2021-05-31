jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICompanyDept, CompanyDept } from '../company-dept.model';
import { CompanyDeptService } from '../service/company-dept.service';

import { CompanyDeptRoutingResolveService } from './company-dept-routing-resolve.service';

describe('Service Tests', () => {
  describe('CompanyDept routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CompanyDeptRoutingResolveService;
    let service: CompanyDeptService;
    let resultCompanyDept: ICompanyDept | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CompanyDeptRoutingResolveService);
      service = TestBed.inject(CompanyDeptService);
      resultCompanyDept = undefined;
    });

    describe('resolve', () => {
      it('should return ICompanyDept returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyDept = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompanyDept).toEqual({ id: 123 });
      });

      it('should return new ICompanyDept if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyDept = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCompanyDept).toEqual(new CompanyDept());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyDept = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompanyDept).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
