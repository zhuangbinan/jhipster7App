import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CommunityImageGroupComponent } from './list/community-image-group.component';
import { CommunityImageGroupDetailComponent } from './detail/community-image-group-detail.component';
import { CommunityImageGroupUpdateComponent } from './update/community-image-group-update.component';
import { CommunityImageGroupDeleteDialogComponent } from './delete/community-image-group-delete-dialog.component';
import { CommunityImageGroupRoutingModule } from './route/community-image-group-routing.module';

@NgModule({
  imports: [SharedModule, CommunityImageGroupRoutingModule],
  declarations: [
    CommunityImageGroupComponent,
    CommunityImageGroupDetailComponent,
    CommunityImageGroupUpdateComponent,
    CommunityImageGroupDeleteDialogComponent,
  ],
  entryComponents: [CommunityImageGroupDeleteDialogComponent],
})
export class CommunityImageGroupModule {}
