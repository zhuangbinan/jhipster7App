import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RoomAddrComponent } from './list/room-addr.component';
import { RoomAddrDetailComponent } from './detail/room-addr-detail.component';
import { RoomAddrUpdateComponent } from './update/room-addr-update.component';
import { RoomAddrDeleteDialogComponent } from './delete/room-addr-delete-dialog.component';
import { RoomAddrRoutingModule } from './route/room-addr-routing.module';

@NgModule({
  imports: [SharedModule, RoomAddrRoutingModule],
  declarations: [RoomAddrComponent, RoomAddrDetailComponent, RoomAddrUpdateComponent, RoomAddrDeleteDialogComponent],
  entryComponents: [RoomAddrDeleteDialogComponent],
})
export class RoomAddrModule {}
