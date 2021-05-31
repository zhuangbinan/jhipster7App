import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommunityNotice } from '../community-notice.model';
import { CommunityNoticeService } from '../service/community-notice.service';

@Component({
  templateUrl: './community-notice-delete-dialog.component.html',
})
export class CommunityNoticeDeleteDialogComponent {
  communityNotice?: ICommunityNotice;

  constructor(protected communityNoticeService: CommunityNoticeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.communityNoticeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
