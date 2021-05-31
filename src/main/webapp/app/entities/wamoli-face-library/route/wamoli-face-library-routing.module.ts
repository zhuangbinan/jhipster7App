import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WamoliFaceLibraryComponent } from '../list/wamoli-face-library.component';
import { WamoliFaceLibraryDetailComponent } from '../detail/wamoli-face-library-detail.component';
import { WamoliFaceLibraryUpdateComponent } from '../update/wamoli-face-library-update.component';
import { WamoliFaceLibraryRoutingResolveService } from './wamoli-face-library-routing-resolve.service';

const wamoliFaceLibraryRoute: Routes = [
  {
    path: '',
    component: WamoliFaceLibraryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WamoliFaceLibraryDetailComponent,
    resolve: {
      wamoliFaceLibrary: WamoliFaceLibraryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WamoliFaceLibraryUpdateComponent,
    resolve: {
      wamoliFaceLibrary: WamoliFaceLibraryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WamoliFaceLibraryUpdateComponent,
    resolve: {
      wamoliFaceLibrary: WamoliFaceLibraryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wamoliFaceLibraryRoute)],
  exports: [RouterModule],
})
export class WamoliFaceLibraryRoutingModule {}
