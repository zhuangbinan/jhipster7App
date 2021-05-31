import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommunityNotice, CommunityNotice } from '../community-notice.model';
import { CommunityNoticeService } from '../service/community-notice.service';

@Injectable({ providedIn: 'root' })
export class CommunityNoticeRoutingResolveService implements Resolve<ICommunityNotice> {
  constructor(protected service: CommunityNoticeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommunityNotice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((communityNotice: HttpResponse<CommunityNotice>) => {
          if (communityNotice.body) {
            return of(communityNotice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CommunityNotice());
  }
}
