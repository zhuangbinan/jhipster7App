import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommunityImages, CommunityImages } from '../community-images.model';
import { CommunityImagesService } from '../service/community-images.service';

@Injectable({ providedIn: 'root' })
export class CommunityImagesRoutingResolveService implements Resolve<ICommunityImages> {
  constructor(protected service: CommunityImagesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommunityImages> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((communityImages: HttpResponse<CommunityImages>) => {
          if (communityImages.body) {
            return of(communityImages.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CommunityImages());
  }
}
