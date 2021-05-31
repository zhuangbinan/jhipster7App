import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHomelandStation } from '../homeland-station.model';
import { HomelandStationService } from '../service/homeland-station.service';

@Component({
  templateUrl: './homeland-station-delete-dialog.component.html',
})
export class HomelandStationDeleteDialogComponent {
  homelandStation?: IHomelandStation;

  constructor(protected homelandStationService: HomelandStationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.homelandStationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
