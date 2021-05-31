import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommunityImages } from '../community-images.model';
import { CommunityImagesService } from '../service/community-images.service';

@Component({
  templateUrl: './community-images-delete-dialog.component.html',
})
export class CommunityImagesDeleteDialogComponent {
  communityImages?: ICommunityImages;

  constructor(protected communityImagesService: CommunityImagesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.communityImagesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
