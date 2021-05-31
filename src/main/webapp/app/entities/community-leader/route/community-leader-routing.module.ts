import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommunityLeaderComponent } from '../list/community-leader.component';
import { CommunityLeaderDetailComponent } from '../detail/community-leader-detail.component';
import { CommunityLeaderUpdateComponent } from '../update/community-leader-update.component';
import { CommunityLeaderRoutingResolveService } from './community-leader-routing-resolve.service';

const communityLeaderRoute: Routes = [
  {
    path: '',
    component: CommunityLeaderComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommunityLeaderDetailComponent,
    resolve: {
      communityLeader: CommunityLeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommunityLeaderUpdateComponent,
    resolve: {
      communityLeader: CommunityLeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommunityLeaderUpdateComponent,
    resolve: {
      communityLeader: CommunityLeaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(communityLeaderRoute)],
  exports: [RouterModule],
})
export class CommunityLeaderRoutingModule {}
