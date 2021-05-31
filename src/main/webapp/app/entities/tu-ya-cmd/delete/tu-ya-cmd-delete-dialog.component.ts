import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITuYaCmd } from '../tu-ya-cmd.model';
import { TuYaCmdService } from '../service/tu-ya-cmd.service';

@Component({
  templateUrl: './tu-ya-cmd-delete-dialog.component.html',
})
export class TuYaCmdDeleteDialogComponent {
  tuYaCmd?: ITuYaCmd;

  constructor(protected tuYaCmdService: TuYaCmdService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tuYaCmdService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
