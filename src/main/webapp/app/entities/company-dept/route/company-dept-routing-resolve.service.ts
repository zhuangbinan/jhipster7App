import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompanyDept, CompanyDept } from '../company-dept.model';
import { CompanyDeptService } from '../service/company-dept.service';

@Injectable({ providedIn: 'root' })
export class CompanyDeptRoutingResolveService implements Resolve<ICompanyDept> {
  constructor(protected service: CompanyDeptService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompanyDept> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((companyDept: HttpResponse<CompanyDept>) => {
          if (companyDept.body) {
            return of(companyDept.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CompanyDept());
  }
}
