jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRoomAddr, RoomAddr } from '../room-addr.model';
import { RoomAddrService } from '../service/room-addr.service';

import { RoomAddrRoutingResolveService } from './room-addr-routing-resolve.service';

describe('Service Tests', () => {
  describe('RoomAddr routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RoomAddrRoutingResolveService;
    let service: RoomAddrService;
    let resultRoomAddr: IRoomAddr | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RoomAddrRoutingResolveService);
      service = TestBed.inject(RoomAddrService);
      resultRoomAddr = undefined;
    });

    describe('resolve', () => {
      it('should return IRoomAddr returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoomAddr = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRoomAddr).toEqual({ id: 123 });
      });

      it('should return new IRoomAddr if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoomAddr = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRoomAddr).toEqual(new RoomAddr());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRoomAddr = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRoomAddr).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
