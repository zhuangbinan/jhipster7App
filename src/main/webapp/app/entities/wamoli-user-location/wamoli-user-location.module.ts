import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { WamoliUserLocationComponent } from './list/wamoli-user-location.component';
import { WamoliUserLocationDetailComponent } from './detail/wamoli-user-location-detail.component';
import { WamoliUserLocationUpdateComponent } from './update/wamoli-user-location-update.component';
import { WamoliUserLocationDeleteDialogComponent } from './delete/wamoli-user-location-delete-dialog.component';
import { WamoliUserLocationRoutingModule } from './route/wamoli-user-location-routing.module';

@NgModule({
  imports: [SharedModule, WamoliUserLocationRoutingModule],
  declarations: [
    WamoliUserLocationComponent,
    WamoliUserLocationDetailComponent,
    WamoliUserLocationUpdateComponent,
    WamoliUserLocationDeleteDialogComponent,
  ],
  entryComponents: [WamoliUserLocationDeleteDialogComponent],
})
export class WamoliUserLocationModule {}
