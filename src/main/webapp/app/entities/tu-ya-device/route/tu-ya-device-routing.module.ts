import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TuYaDeviceComponent } from '../list/tu-ya-device.component';
import { TuYaDeviceDetailComponent } from '../detail/tu-ya-device-detail.component';
import { TuYaDeviceUpdateComponent } from '../update/tu-ya-device-update.component';
import { TuYaDeviceRoutingResolveService } from './tu-ya-device-routing-resolve.service';

const tuYaDeviceRoute: Routes = [
  {
    path: '',
    component: TuYaDeviceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TuYaDeviceDetailComponent,
    resolve: {
      tuYaDevice: TuYaDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TuYaDeviceUpdateComponent,
    resolve: {
      tuYaDevice: TuYaDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TuYaDeviceUpdateComponent,
    resolve: {
      tuYaDevice: TuYaDeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tuYaDeviceRoute)],
  exports: [RouterModule],
})
export class TuYaDeviceRoutingModule {}
