import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoomAddr, RoomAddr } from '../room-addr.model';
import { RoomAddrService } from '../service/room-addr.service';

@Injectable({ providedIn: 'root' })
export class RoomAddrRoutingResolveService implements Resolve<IRoomAddr> {
  constructor(protected service: RoomAddrService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoomAddr> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roomAddr: HttpResponse<RoomAddr>) => {
          if (roomAddr.body) {
            return of(roomAddr.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RoomAddr());
  }
}
