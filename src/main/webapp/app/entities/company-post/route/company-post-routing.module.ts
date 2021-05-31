import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompanyPostComponent } from '../list/company-post.component';
import { CompanyPostDetailComponent } from '../detail/company-post-detail.component';
import { CompanyPostUpdateComponent } from '../update/company-post-update.component';
import { CompanyPostRoutingResolveService } from './company-post-routing-resolve.service';

const companyPostRoute: Routes = [
  {
    path: '',
    component: CompanyPostComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompanyPostDetailComponent,
    resolve: {
      companyPost: CompanyPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompanyPostUpdateComponent,
    resolve: {
      companyPost: CompanyPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompanyPostUpdateComponent,
    resolve: {
      companyPost: CompanyPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(companyPostRoute)],
  exports: [RouterModule],
})
export class CompanyPostRoutingModule {}
