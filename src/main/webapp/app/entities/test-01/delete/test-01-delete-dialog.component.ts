import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITest01 } from '../test-01.model';
import { Test01Service } from '../service/test-01.service';

@Component({
  templateUrl: './test-01-delete-dialog.component.html',
})
export class Test01DeleteDialogComponent {
  test01?: ITest01;

  constructor(protected test01Service: Test01Service, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.test01Service.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
