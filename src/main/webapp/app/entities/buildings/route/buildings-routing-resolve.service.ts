import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBuildings, Buildings } from '../buildings.model';
import { BuildingsService } from '../service/buildings.service';

@Injectable({ providedIn: 'root' })
export class BuildingsRoutingResolveService implements Resolve<IBuildings> {
  constructor(protected service: BuildingsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBuildings> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((buildings: HttpResponse<Buildings>) => {
          if (buildings.body) {
            return of(buildings.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Buildings());
  }
}
