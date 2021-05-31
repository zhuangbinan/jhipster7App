import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompanyDept } from '../company-dept.model';
import { CompanyDeptService } from '../service/company-dept.service';

@Component({
  templateUrl: './company-dept-delete-dialog.component.html',
})
export class CompanyDeptDeleteDialogComponent {
  companyDept?: ICompanyDept;

  constructor(protected companyDeptService: CompanyDeptService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.companyDeptService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
