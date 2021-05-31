import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WamoliUserComponent } from '../list/wamoli-user.component';
import { WamoliUserDetailComponent } from '../detail/wamoli-user-detail.component';
import { WamoliUserUpdateComponent } from '../update/wamoli-user-update.component';
import { WamoliUserRoutingResolveService } from './wamoli-user-routing-resolve.service';

const wamoliUserRoute: Routes = [
  {
    path: '',
    component: WamoliUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WamoliUserDetailComponent,
    resolve: {
      wamoliUser: WamoliUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WamoliUserUpdateComponent,
    resolve: {
      wamoliUser: WamoliUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WamoliUserUpdateComponent,
    resolve: {
      wamoliUser: WamoliUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wamoliUserRoute)],
  exports: [RouterModule],
})
export class WamoliUserRoutingModule {}
