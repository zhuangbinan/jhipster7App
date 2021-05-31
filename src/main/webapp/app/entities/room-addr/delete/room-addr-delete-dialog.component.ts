import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoomAddr } from '../room-addr.model';
import { RoomAddrService } from '../service/room-addr.service';

@Component({
  templateUrl: './room-addr-delete-dialog.component.html',
})
export class RoomAddrDeleteDialogComponent {
  roomAddr?: IRoomAddr;

  constructor(protected roomAddrService: RoomAddrService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roomAddrService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
