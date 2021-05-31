import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BuildingsComponent } from '../list/buildings.component';
import { BuildingsDetailComponent } from '../detail/buildings-detail.component';
import { BuildingsUpdateComponent } from '../update/buildings-update.component';
import { BuildingsRoutingResolveService } from './buildings-routing-resolve.service';

const buildingsRoute: Routes = [
  {
    path: '',
    component: BuildingsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BuildingsDetailComponent,
    resolve: {
      buildings: BuildingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BuildingsUpdateComponent,
    resolve: {
      buildings: BuildingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BuildingsUpdateComponent,
    resolve: {
      buildings: BuildingsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(buildingsRoute)],
  exports: [RouterModule],
})
export class BuildingsRoutingModule {}
