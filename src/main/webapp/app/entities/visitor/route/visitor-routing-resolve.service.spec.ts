jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVisitor, Visitor } from '../visitor.model';
import { VisitorService } from '../service/visitor.service';

import { VisitorRoutingResolveService } from './visitor-routing-resolve.service';

describe('Service Tests', () => {
  describe('Visitor routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VisitorRoutingResolveService;
    let service: VisitorService;
    let resultVisitor: IVisitor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VisitorRoutingResolveService);
      service = TestBed.inject(VisitorService);
      resultVisitor = undefined;
    });

    describe('resolve', () => {
      it('should return IVisitor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisitor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVisitor).toEqual({ id: 123 });
      });

      it('should return new IVisitor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisitor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVisitor).toEqual(new Visitor());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisitor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVisitor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
