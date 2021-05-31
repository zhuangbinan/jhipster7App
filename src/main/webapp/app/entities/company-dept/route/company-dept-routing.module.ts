import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompanyDeptComponent } from '../list/company-dept.component';
import { CompanyDeptDetailComponent } from '../detail/company-dept-detail.component';
import { CompanyDeptUpdateComponent } from '../update/company-dept-update.component';
import { CompanyDeptRoutingResolveService } from './company-dept-routing-resolve.service';

const companyDeptRoute: Routes = [
  {
    path: '',
    component: CompanyDeptComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompanyDeptDetailComponent,
    resolve: {
      companyDept: CompanyDeptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompanyDeptUpdateComponent,
    resolve: {
      companyDept: CompanyDeptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompanyDeptUpdateComponent,
    resolve: {
      companyDept: CompanyDeptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(companyDeptRoute)],
  exports: [RouterModule],
})
export class CompanyDeptRoutingModule {}
