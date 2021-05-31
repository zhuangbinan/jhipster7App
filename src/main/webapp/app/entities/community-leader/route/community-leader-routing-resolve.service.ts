import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommunityLeader, CommunityLeader } from '../community-leader.model';
import { CommunityLeaderService } from '../service/community-leader.service';

@Injectable({ providedIn: 'root' })
export class CommunityLeaderRoutingResolveService implements Resolve<ICommunityLeader> {
  constructor(protected service: CommunityLeaderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommunityLeader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((communityLeader: HttpResponse<CommunityLeader>) => {
          if (communityLeader.body) {
            return of(communityLeader.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CommunityLeader());
  }
}
