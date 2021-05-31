import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITest01, Test01 } from '../test-01.model';
import { Test01Service } from '../service/test-01.service';

@Injectable({ providedIn: 'root' })
export class Test01RoutingResolveService implements Resolve<ITest01> {
  constructor(protected service: Test01Service, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITest01> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((test01: HttpResponse<Test01>) => {
          if (test01.body) {
            return of(test01.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Test01());
  }
}
