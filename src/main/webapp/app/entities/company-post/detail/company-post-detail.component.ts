import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompanyPost } from '../company-post.model';

@Component({
  selector: 'jhi-company-post-detail',
  templateUrl: './company-post-detail.component.html',
})
export class CompanyPostDetailComponent implements OnInit {
  companyPost: ICompanyPost | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyPost }) => {
      this.companyPost = companyPost;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
