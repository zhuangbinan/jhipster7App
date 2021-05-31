import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWamoliFaceLibrary, WamoliFaceLibrary } from '../wamoli-face-library.model';
import { WamoliFaceLibraryService } from '../service/wamoli-face-library.service';

@Injectable({ providedIn: 'root' })
export class WamoliFaceLibraryRoutingResolveService implements Resolve<IWamoliFaceLibrary> {
  constructor(protected service: WamoliFaceLibraryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWamoliFaceLibrary> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wamoliFaceLibrary: HttpResponse<WamoliFaceLibrary>) => {
          if (wamoliFaceLibrary.body) {
            return of(wamoliFaceLibrary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WamoliFaceLibrary());
  }
}
