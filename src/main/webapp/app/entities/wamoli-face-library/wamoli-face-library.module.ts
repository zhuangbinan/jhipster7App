import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { WamoliFaceLibraryComponent } from './list/wamoli-face-library.component';
import { WamoliFaceLibraryDetailComponent } from './detail/wamoli-face-library-detail.component';
import { WamoliFaceLibraryUpdateComponent } from './update/wamoli-face-library-update.component';
import { WamoliFaceLibraryDeleteDialogComponent } from './delete/wamoli-face-library-delete-dialog.component';
import { WamoliFaceLibraryRoutingModule } from './route/wamoli-face-library-routing.module';

@NgModule({
  imports: [SharedModule, WamoliFaceLibraryRoutingModule],
  declarations: [
    WamoliFaceLibraryComponent,
    WamoliFaceLibraryDetailComponent,
    WamoliFaceLibraryUpdateComponent,
    WamoliFaceLibraryDeleteDialogComponent,
  ],
  entryComponents: [WamoliFaceLibraryDeleteDialogComponent],
})
export class WamoliFaceLibraryModule {}
