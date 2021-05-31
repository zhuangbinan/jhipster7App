import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoomAddrComponent } from '../list/room-addr.component';
import { RoomAddrDetailComponent } from '../detail/room-addr-detail.component';
import { RoomAddrUpdateComponent } from '../update/room-addr-update.component';
import { RoomAddrRoutingResolveService } from './room-addr-routing-resolve.service';

const roomAddrRoute: Routes = [
  {
    path: '',
    component: RoomAddrComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomAddrDetailComponent,
    resolve: {
      roomAddr: RoomAddrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomAddrUpdateComponent,
    resolve: {
      roomAddr: RoomAddrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomAddrUpdateComponent,
    resolve: {
      roomAddr: RoomAddrRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roomAddrRoute)],
  exports: [RouterModule],
})
export class RoomAddrRoutingModule {}
