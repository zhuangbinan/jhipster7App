import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { HomelandStationComponent } from './list/homeland-station.component';
import { HomelandStationDetailComponent } from './detail/homeland-station-detail.component';
import { HomelandStationUpdateComponent } from './update/homeland-station-update.component';
import { HomelandStationDeleteDialogComponent } from './delete/homeland-station-delete-dialog.component';
import { HomelandStationRoutingModule } from './route/homeland-station-routing.module';

@NgModule({
  imports: [SharedModule, HomelandStationRoutingModule],
  declarations: [
    HomelandStationComponent,
    HomelandStationDetailComponent,
    HomelandStationUpdateComponent,
    HomelandStationDeleteDialogComponent,
  ],
  entryComponents: [HomelandStationDeleteDialogComponent],
})
export class HomelandStationModule {}
