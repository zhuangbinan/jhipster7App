import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompanyPost, CompanyPost } from '../company-post.model';
import { CompanyPostService } from '../service/company-post.service';

@Component({
  selector: 'jhi-company-post-update',
  templateUrl: './company-post-update.component.html',
})
export class CompanyPostUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    postCode: [],
    postName: [],
    orderNum: [],
    remark: [],
    enable: [],
    createBy: [],
    createDate: [],
    lastModifyBy: [],
    lastModifyDate: [],
  });

  constructor(protected companyPostService: CompanyPostService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyPost }) => {
      if (companyPost.id === undefined) {
        const today = dayjs().startOf('day');
        companyPost.createDate = today;
        companyPost.lastModifyDate = today;
      }

      this.updateForm(companyPost);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const companyPost = this.createFromForm();
    if (companyPost.id !== undefined) {
      this.subscribeToSaveResponse(this.companyPostService.update(companyPost));
    } else {
      this.subscribeToSaveResponse(this.companyPostService.create(companyPost));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyPost>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(companyPost: ICompanyPost): void {
    this.editForm.patchValue({
      id: companyPost.id,
      postCode: companyPost.postCode,
      postName: companyPost.postName,
      orderNum: companyPost.orderNum,
      remark: companyPost.remark,
      enable: companyPost.enable,
      createBy: companyPost.createBy,
      createDate: companyPost.createDate ? companyPost.createDate.format(DATE_TIME_FORMAT) : null,
      lastModifyBy: companyPost.lastModifyBy,
      lastModifyDate: companyPost.lastModifyDate ? companyPost.lastModifyDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ICompanyPost {
    return {
      ...new CompanyPost(),
      id: this.editForm.get(['id'])!.value,
      postCode: this.editForm.get(['postCode'])!.value,
      postName: this.editForm.get(['postName'])!.value,
      orderNum: this.editForm.get(['orderNum'])!.value,
      remark: this.editForm.get(['remark'])!.value,
      enable: this.editForm.get(['enable'])!.value,
      createBy: this.editForm.get(['createBy'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastModifyBy: this.editForm.get(['lastModifyBy'])!.value,
      lastModifyDate: this.editForm.get(['lastModifyDate'])!.value
        ? dayjs(this.editForm.get(['lastModifyDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
