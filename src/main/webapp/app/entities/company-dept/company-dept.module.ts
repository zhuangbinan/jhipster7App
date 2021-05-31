import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CompanyDeptComponent } from './list/company-dept.component';
import { CompanyDeptDetailComponent } from './detail/company-dept-detail.component';
import { CompanyDeptUpdateComponent } from './update/company-dept-update.component';
import { CompanyDeptDeleteDialogComponent } from './delete/company-dept-delete-dialog.component';
import { CompanyDeptRoutingModule } from './route/company-dept-routing.module';

@NgModule({
  imports: [SharedModule, CompanyDeptRoutingModule],
  declarations: [CompanyDeptComponent, CompanyDeptDetailComponent, CompanyDeptUpdateComponent, CompanyDeptDeleteDialogComponent],
  entryComponents: [CompanyDeptDeleteDialogComponent],
})
export class CompanyDeptModule {}
