jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITuYaCmd, TuYaCmd } from '../tu-ya-cmd.model';
import { TuYaCmdService } from '../service/tu-ya-cmd.service';

import { TuYaCmdRoutingResolveService } from './tu-ya-cmd-routing-resolve.service';

describe('Service Tests', () => {
  describe('TuYaCmd routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TuYaCmdRoutingResolveService;
    let service: TuYaCmdService;
    let resultTuYaCmd: ITuYaCmd | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TuYaCmdRoutingResolveService);
      service = TestBed.inject(TuYaCmdService);
      resultTuYaCmd = undefined;
    });

    describe('resolve', () => {
      it('should return ITuYaCmd returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTuYaCmd = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTuYaCmd).toEqual({ id: 123 });
      });

      it('should return new ITuYaCmd if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTuYaCmd = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTuYaCmd).toEqual(new TuYaCmd());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTuYaCmd = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTuYaCmd).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
