jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICompanyPost, CompanyPost } from '../company-post.model';
import { CompanyPostService } from '../service/company-post.service';

import { CompanyPostRoutingResolveService } from './company-post-routing-resolve.service';

describe('Service Tests', () => {
  describe('CompanyPost routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CompanyPostRoutingResolveService;
    let service: CompanyPostService;
    let resultCompanyPost: ICompanyPost | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CompanyPostRoutingResolveService);
      service = TestBed.inject(CompanyPostService);
      resultCompanyPost = undefined;
    });

    describe('resolve', () => {
      it('should return ICompanyPost returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyPost = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompanyPost).toEqual({ id: 123 });
      });

      it('should return new ICompanyPost if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyPost = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCompanyPost).toEqual(new CompanyPost());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanyPost = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompanyPost).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
