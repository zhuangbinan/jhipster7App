import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HomelandStationComponent } from '../list/homeland-station.component';
import { HomelandStationDetailComponent } from '../detail/homeland-station-detail.component';
import { HomelandStationUpdateComponent } from '../update/homeland-station-update.component';
import { HomelandStationRoutingResolveService } from './homeland-station-routing-resolve.service';

const homelandStationRoute: Routes = [
  {
    path: '',
    component: HomelandStationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HomelandStationDetailComponent,
    resolve: {
      homelandStation: HomelandStationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HomelandStationUpdateComponent,
    resolve: {
      homelandStation: HomelandStationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HomelandStationUpdateComponent,
    resolve: {
      homelandStation: HomelandStationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(homelandStationRoute)],
  exports: [RouterModule],
})
export class HomelandStationRoutingModule {}
