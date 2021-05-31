import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Test01Component } from '../list/test-01.component';
import { Test01DetailComponent } from '../detail/test-01-detail.component';
import { Test01UpdateComponent } from '../update/test-01-update.component';
import { Test01RoutingResolveService } from './test-01-routing-resolve.service';

const test01Route: Routes = [
  {
    path: '',
    component: Test01Component,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: Test01DetailComponent,
    resolve: {
      test01: Test01RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: Test01UpdateComponent,
    resolve: {
      test01: Test01RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: Test01UpdateComponent,
    resolve: {
      test01: Test01RoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(test01Route)],
  exports: [RouterModule],
})
export class Test01RoutingModule {}
