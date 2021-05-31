import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TuYaCmdComponent } from './list/tu-ya-cmd.component';
import { TuYaCmdDetailComponent } from './detail/tu-ya-cmd-detail.component';
import { TuYaCmdUpdateComponent } from './update/tu-ya-cmd-update.component';
import { TuYaCmdDeleteDialogComponent } from './delete/tu-ya-cmd-delete-dialog.component';
import { TuYaCmdRoutingModule } from './route/tu-ya-cmd-routing.module';

@NgModule({
  imports: [SharedModule, TuYaCmdRoutingModule],
  declarations: [TuYaCmdComponent, TuYaCmdDetailComponent, TuYaCmdUpdateComponent, TuYaCmdDeleteDialogComponent],
  entryComponents: [TuYaCmdDeleteDialogComponent],
})
export class TuYaCmdModule {}
