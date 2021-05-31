import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompanyDept } from '../company-dept.model';

@Component({
  selector: 'jhi-company-dept-detail',
  templateUrl: './company-dept-detail.component.html',
})
export class CompanyDeptDetailComponent implements OnInit {
  companyDept: ICompanyDept | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyDept }) => {
      this.companyDept = companyDept;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
