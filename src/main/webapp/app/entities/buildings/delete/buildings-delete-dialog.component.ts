import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBuildings } from '../buildings.model';
import { BuildingsService } from '../service/buildings.service';

@Component({
  templateUrl: './buildings-delete-dialog.component.html',
})
export class BuildingsDeleteDialogComponent {
  buildings?: IBuildings;

  constructor(protected buildingsService: BuildingsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.buildingsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
