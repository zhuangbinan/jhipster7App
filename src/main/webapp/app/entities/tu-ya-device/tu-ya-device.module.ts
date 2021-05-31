import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TuYaDeviceComponent } from './list/tu-ya-device.component';
import { TuYaDeviceDetailComponent } from './detail/tu-ya-device-detail.component';
import { TuYaDeviceUpdateComponent } from './update/tu-ya-device-update.component';
import { TuYaDeviceDeleteDialogComponent } from './delete/tu-ya-device-delete-dialog.component';
import { TuYaDeviceRoutingModule } from './route/tu-ya-device-routing.module';

@NgModule({
  imports: [SharedModule, TuYaDeviceRoutingModule],
  declarations: [TuYaDeviceComponent, TuYaDeviceDetailComponent, TuYaDeviceUpdateComponent, TuYaDeviceDeleteDialogComponent],
  entryComponents: [TuYaDeviceDeleteDialogComponent],
})
export class TuYaDeviceModule {}
