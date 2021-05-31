import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWamoliUserLocation, WamoliUserLocation } from '../wamoli-user-location.model';
import { WamoliUserLocationService } from '../service/wamoli-user-location.service';

@Injectable({ providedIn: 'root' })
export class WamoliUserLocationRoutingResolveService implements Resolve<IWamoliUserLocation> {
  constructor(protected service: WamoliUserLocationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWamoliUserLocation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wamoliUserLocation: HttpResponse<WamoliUserLocation>) => {
          if (wamoliUserLocation.body) {
            return of(wamoliUserLocation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WamoliUserLocation());
  }
}
