import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CommunityLeaderComponent } from './list/community-leader.component';
import { CommunityLeaderDetailComponent } from './detail/community-leader-detail.component';
import { CommunityLeaderUpdateComponent } from './update/community-leader-update.component';
import { CommunityLeaderDeleteDialogComponent } from './delete/community-leader-delete-dialog.component';
import { CommunityLeaderRoutingModule } from './route/community-leader-routing.module';

@NgModule({
  imports: [SharedModule, CommunityLeaderRoutingModule],
  declarations: [
    CommunityLeaderComponent,
    CommunityLeaderDetailComponent,
    CommunityLeaderUpdateComponent,
    CommunityLeaderDeleteDialogComponent,
  ],
  entryComponents: [CommunityLeaderDeleteDialogComponent],
})
export class CommunityLeaderModule {}
