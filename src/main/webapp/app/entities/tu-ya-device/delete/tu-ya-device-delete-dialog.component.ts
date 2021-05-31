import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITuYaDevice } from '../tu-ya-device.model';
import { TuYaDeviceService } from '../service/tu-ya-device.service';

@Component({
  templateUrl: './tu-ya-device-delete-dialog.component.html',
})
export class TuYaDeviceDeleteDialogComponent {
  tuYaDevice?: ITuYaDevice;

  constructor(protected tuYaDeviceService: TuYaDeviceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tuYaDeviceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
