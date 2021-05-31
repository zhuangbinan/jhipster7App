import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CommunityNoticeComponent } from './list/community-notice.component';
import { CommunityNoticeDetailComponent } from './detail/community-notice-detail.component';
import { CommunityNoticeUpdateComponent } from './update/community-notice-update.component';
import { CommunityNoticeDeleteDialogComponent } from './delete/community-notice-delete-dialog.component';
import { CommunityNoticeRoutingModule } from './route/community-notice-routing.module';

@NgModule({
  imports: [SharedModule, CommunityNoticeRoutingModule],
  declarations: [
    CommunityNoticeComponent,
    CommunityNoticeDetailComponent,
    CommunityNoticeUpdateComponent,
    CommunityNoticeDeleteDialogComponent,
  ],
  entryComponents: [CommunityNoticeDeleteDialogComponent],
})
export class CommunityNoticeModule {}
