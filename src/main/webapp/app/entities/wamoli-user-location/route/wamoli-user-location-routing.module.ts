import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WamoliUserLocationComponent } from '../list/wamoli-user-location.component';
import { WamoliUserLocationDetailComponent } from '../detail/wamoli-user-location-detail.component';
import { WamoliUserLocationUpdateComponent } from '../update/wamoli-user-location-update.component';
import { WamoliUserLocationRoutingResolveService } from './wamoli-user-location-routing-resolve.service';

const wamoliUserLocationRoute: Routes = [
  {
    path: '',
    component: WamoliUserLocationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WamoliUserLocationDetailComponent,
    resolve: {
      wamoliUserLocation: WamoliUserLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WamoliUserLocationUpdateComponent,
    resolve: {
      wamoliUserLocation: WamoliUserLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WamoliUserLocationUpdateComponent,
    resolve: {
      wamoliUserLocation: WamoliUserLocationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wamoliUserLocationRoute)],
  exports: [RouterModule],
})
export class WamoliUserLocationRoutingModule {}
