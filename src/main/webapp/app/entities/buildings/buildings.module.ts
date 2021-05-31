import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BuildingsComponent } from './list/buildings.component';
import { BuildingsDetailComponent } from './detail/buildings-detail.component';
import { BuildingsUpdateComponent } from './update/buildings-update.component';
import { BuildingsDeleteDialogComponent } from './delete/buildings-delete-dialog.component';
import { BuildingsRoutingModule } from './route/buildings-routing.module';

@NgModule({
  imports: [SharedModule, BuildingsRoutingModule],
  declarations: [BuildingsComponent, BuildingsDetailComponent, BuildingsUpdateComponent, BuildingsDeleteDialogComponent],
  entryComponents: [BuildingsDeleteDialogComponent],
})
export class BuildingsModule {}
