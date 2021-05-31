import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompanyPost } from '../company-post.model';
import { CompanyPostService } from '../service/company-post.service';

@Component({
  templateUrl: './company-post-delete-dialog.component.html',
})
export class CompanyPostDeleteDialogComponent {
  companyPost?: ICompanyPost;

  constructor(protected companyPostService: CompanyPostService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.companyPostService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
