import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommunityNoticeComponent } from '../list/community-notice.component';
import { CommunityNoticeDetailComponent } from '../detail/community-notice-detail.component';
import { CommunityNoticeUpdateComponent } from '../update/community-notice-update.component';
import { CommunityNoticeRoutingResolveService } from './community-notice-routing-resolve.service';

const communityNoticeRoute: Routes = [
  {
    path: '',
    component: CommunityNoticeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommunityNoticeDetailComponent,
    resolve: {
      communityNotice: CommunityNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommunityNoticeUpdateComponent,
    resolve: {
      communityNotice: CommunityNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommunityNoticeUpdateComponent,
    resolve: {
      communityNotice: CommunityNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(communityNoticeRoute)],
  exports: [RouterModule],
})
export class CommunityNoticeRoutingModule {}
