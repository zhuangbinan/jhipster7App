import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWamoliUserLocation } from '../wamoli-user-location.model';
import { WamoliUserLocationService } from '../service/wamoli-user-location.service';

@Component({
  templateUrl: './wamoli-user-location-delete-dialog.component.html',
})
export class WamoliUserLocationDeleteDialogComponent {
  wamoliUserLocation?: IWamoliUserLocation;

  constructor(protected wamoliUserLocationService: WamoliUserLocationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wamoliUserLocationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
