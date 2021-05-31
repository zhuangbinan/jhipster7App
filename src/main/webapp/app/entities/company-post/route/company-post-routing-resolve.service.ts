import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompanyPost, CompanyPost } from '../company-post.model';
import { CompanyPostService } from '../service/company-post.service';

@Injectable({ providedIn: 'root' })
export class CompanyPostRoutingResolveService implements Resolve<ICompanyPost> {
  constructor(protected service: CompanyPostService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompanyPost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((companyPost: HttpResponse<CompanyPost>) => {
          if (companyPost.body) {
            return of(companyPost.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CompanyPost());
  }
}
