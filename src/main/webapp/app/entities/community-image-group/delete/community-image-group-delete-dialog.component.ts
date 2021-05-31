import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommunityImageGroup } from '../community-image-group.model';
import { CommunityImageGroupService } from '../service/community-image-group.service';

@Component({
  templateUrl: './community-image-group-delete-dialog.component.html',
})
export class CommunityImageGroupDeleteDialogComponent {
  communityImageGroup?: ICommunityImageGroup;

  constructor(protected communityImageGroupService: CommunityImageGroupService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.communityImageGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
