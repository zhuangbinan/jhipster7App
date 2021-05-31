import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CommunityImagesComponent } from './list/community-images.component';
import { CommunityImagesDetailComponent } from './detail/community-images-detail.component';
import { CommunityImagesUpdateComponent } from './update/community-images-update.component';
import { CommunityImagesDeleteDialogComponent } from './delete/community-images-delete-dialog.component';
import { CommunityImagesRoutingModule } from './route/community-images-routing.module';

@NgModule({
  imports: [SharedModule, CommunityImagesRoutingModule],
  declarations: [
    CommunityImagesComponent,
    CommunityImagesDetailComponent,
    CommunityImagesUpdateComponent,
    CommunityImagesDeleteDialogComponent,
  ],
  entryComponents: [CommunityImagesDeleteDialogComponent],
})
export class CommunityImagesModule {}
