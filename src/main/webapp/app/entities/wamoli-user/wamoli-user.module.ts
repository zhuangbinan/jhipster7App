import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { WamoliUserComponent } from './list/wamoli-user.component';
import { WamoliUserDetailComponent } from './detail/wamoli-user-detail.component';
import { WamoliUserUpdateComponent } from './update/wamoli-user-update.component';
import { WamoliUserDeleteDialogComponent } from './delete/wamoli-user-delete-dialog.component';
import { WamoliUserRoutingModule } from './route/wamoli-user-routing.module';

@NgModule({
  imports: [SharedModule, WamoliUserRoutingModule],
  declarations: [WamoliUserComponent, WamoliUserDetailComponent, WamoliUserUpdateComponent, WamoliUserDeleteDialogComponent],
  entryComponents: [WamoliUserDeleteDialogComponent],
})
export class WamoliUserModule {}
