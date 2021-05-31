import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWamoliUser } from '../wamoli-user.model';
import { WamoliUserService } from '../service/wamoli-user.service';

@Component({
  templateUrl: './wamoli-user-delete-dialog.component.html',
})
export class WamoliUserDeleteDialogComponent {
  wamoliUser?: IWamoliUser;

  constructor(protected wamoliUserService: WamoliUserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wamoliUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
