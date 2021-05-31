import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITuYaCmd, TuYaCmd } from '../tu-ya-cmd.model';
import { TuYaCmdService } from '../service/tu-ya-cmd.service';

@Injectable({ providedIn: 'root' })
export class TuYaCmdRoutingResolveService implements Resolve<ITuYaCmd> {
  constructor(protected service: TuYaCmdService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITuYaCmd> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tuYaCmd: HttpResponse<TuYaCmd>) => {
          if (tuYaCmd.body) {
            return of(tuYaCmd.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TuYaCmd());
  }
}
