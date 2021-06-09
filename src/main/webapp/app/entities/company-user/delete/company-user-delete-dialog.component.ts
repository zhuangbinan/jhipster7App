import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompanyUser } from '../company-user.model';
import { CompanyUserService } from '../service/company-user.service';

@Component({
  templateUrl: './company-user-delete-dialog.component.html',
})
export class CompanyUserDeleteDialogComponent {
  companyUser?: ICompanyUser;

  constructor(protected companyUserService: CompanyUserService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.companyUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
