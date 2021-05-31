import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TuYaCmdComponent } from '../list/tu-ya-cmd.component';
import { TuYaCmdDetailComponent } from '../detail/tu-ya-cmd-detail.component';
import { TuYaCmdUpdateComponent } from '../update/tu-ya-cmd-update.component';
import { TuYaCmdRoutingResolveService } from './tu-ya-cmd-routing-resolve.service';

const tuYaCmdRoute: Routes = [
  {
    path: '',
    component: TuYaCmdComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TuYaCmdDetailComponent,
    resolve: {
      tuYaCmd: TuYaCmdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TuYaCmdUpdateComponent,
    resolve: {
      tuYaCmd: TuYaCmdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TuYaCmdUpdateComponent,
    resolve: {
      tuYaCmd: TuYaCmdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tuYaCmdRoute)],
  exports: [RouterModule],
})
export class TuYaCmdRoutingModule {}
