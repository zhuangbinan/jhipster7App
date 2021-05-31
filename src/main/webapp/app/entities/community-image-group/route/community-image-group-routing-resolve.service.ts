import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommunityImageGroup, CommunityImageGroup } from '../community-image-group.model';
import { CommunityImageGroupService } from '../service/community-image-group.service';

@Injectable({ providedIn: 'root' })
export class CommunityImageGroupRoutingResolveService implements Resolve<ICommunityImageGroup> {
  constructor(protected service: CommunityImageGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommunityImageGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((communityImageGroup: HttpResponse<CommunityImageGroup>) => {
          if (communityImageGroup.body) {
            return of(communityImageGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CommunityImageGroup());
  }
}
