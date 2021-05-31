import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CompanyPostComponent } from './list/company-post.component';
import { CompanyPostDetailComponent } from './detail/company-post-detail.component';
import { CompanyPostUpdateComponent } from './update/company-post-update.component';
import { CompanyPostDeleteDialogComponent } from './delete/company-post-delete-dialog.component';
import { CompanyPostRoutingModule } from './route/company-post-routing.module';

@NgModule({
  imports: [SharedModule, CompanyPostRoutingModule],
  declarations: [CompanyPostComponent, CompanyPostDetailComponent, CompanyPostUpdateComponent, CompanyPostDeleteDialogComponent],
  entryComponents: [CompanyPostDeleteDialogComponent],
})
export class CompanyPostModule {}
