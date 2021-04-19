jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAuthor, Author } from '../author.model';
import { AuthorService } from '../service/author.service';

import { AuthorRoutingResolveService } from './author-routing-resolve.service';

describe('Service Tests', () => {
  describe('Author routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AuthorRoutingResolveService;
    let service: AuthorService;
    let resultAuthor: IAuthor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AuthorRoutingResolveService);
      service = TestBed.inject(AuthorService);
      resultAuthor = undefined;
    });

    describe('resolve', () => {
      it('should return IAuthor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAuthor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAuthor).toEqual({ id: 123 });
      });

      it('should return new IAuthor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAuthor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAuthor).toEqual(new Author());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAuthor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAuthor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
