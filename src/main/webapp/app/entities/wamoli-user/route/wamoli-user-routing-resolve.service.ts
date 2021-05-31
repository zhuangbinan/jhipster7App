import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWamoliUser, WamoliUser } from '../wamoli-user.model';
import { WamoliUserService } from '../service/wamoli-user.service';

@Injectable({ providedIn: 'root' })
export class WamoliUserRoutingResolveService implements Resolve<IWamoliUser> {
  constructor(protected service: WamoliUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWamoliUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wamoliUser: HttpResponse<WamoliUser>) => {
          if (wamoliUser.body) {
            return of(wamoliUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WamoliUser());
  }
}
