import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommunityImagesComponent } from '../list/community-images.component';
import { CommunityImagesDetailComponent } from '../detail/community-images-detail.component';
import { CommunityImagesUpdateComponent } from '../update/community-images-update.component';
import { CommunityImagesRoutingResolveService } from './community-images-routing-resolve.service';

const communityImagesRoute: Routes = [
  {
    path: '',
    component: CommunityImagesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommunityImagesDetailComponent,
    resolve: {
      communityImages: CommunityImagesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommunityImagesUpdateComponent,
    resolve: {
      communityImages: CommunityImagesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommunityImagesUpdateComponent,
    resolve: {
      communityImages: CommunityImagesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(communityImagesRoute)],
  exports: [RouterModule],
})
export class CommunityImagesRoutingModule {}
