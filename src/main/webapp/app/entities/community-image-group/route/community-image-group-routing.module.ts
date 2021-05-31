import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommunityImageGroupComponent } from '../list/community-image-group.component';
import { CommunityImageGroupDetailComponent } from '../detail/community-image-group-detail.component';
import { CommunityImageGroupUpdateComponent } from '../update/community-image-group-update.component';
import { CommunityImageGroupRoutingResolveService } from './community-image-group-routing-resolve.service';

const communityImageGroupRoute: Routes = [
  {
    path: '',
    component: CommunityImageGroupComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommunityImageGroupDetailComponent,
    resolve: {
      communityImageGroup: CommunityImageGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommunityImageGroupUpdateComponent,
    resolve: {
      communityImageGroup: CommunityImageGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommunityImageGroupUpdateComponent,
    resolve: {
      communityImageGroup: CommunityImageGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(communityImageGroupRoute)],
  exports: [RouterModule],
})
export class CommunityImageGroupRoutingModule {}
