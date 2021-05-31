import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHomelandStation, HomelandStation } from '../homeland-station.model';
import { HomelandStationService } from '../service/homeland-station.service';

@Injectable({ providedIn: 'root' })
export class HomelandStationRoutingResolveService implements Resolve<IHomelandStation> {
  constructor(protected service: HomelandStationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHomelandStation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((homelandStation: HttpResponse<HomelandStation>) => {
          if (homelandStation.body) {
            return of(homelandStation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HomelandStation());
  }
}
