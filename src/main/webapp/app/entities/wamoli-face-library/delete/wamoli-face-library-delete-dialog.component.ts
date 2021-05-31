import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWamoliFaceLibrary } from '../wamoli-face-library.model';
import { WamoliFaceLibraryService } from '../service/wamoli-face-library.service';

@Component({
  templateUrl: './wamoli-face-library-delete-dialog.component.html',
})
export class WamoliFaceLibraryDeleteDialogComponent {
  wamoliFaceLibrary?: IWamoliFaceLibrary;

  constructor(protected wamoliFaceLibraryService: WamoliFaceLibraryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wamoliFaceLibraryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
