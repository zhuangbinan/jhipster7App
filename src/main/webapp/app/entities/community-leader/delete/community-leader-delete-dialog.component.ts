import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommunityLeader } from '../community-leader.model';
import { CommunityLeaderService } from '../service/community-leader.service';

@Component({
  templateUrl: './community-leader-delete-dialog.component.html',
})
export class CommunityLeaderDeleteDialogComponent {
  communityLeader?: ICommunityLeader;

  constructor(protected communityLeaderService: CommunityLeaderService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.communityLeaderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
