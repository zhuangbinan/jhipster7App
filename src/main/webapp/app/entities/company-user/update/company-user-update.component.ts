import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompanyUser, CompanyUser } from '../company-user.model';
import { CompanyUserService } from '../service/company-user.service';

@Component({
  selector: 'jhi-company-user-update',
  templateUrl: './company-user-update.component.html',
})
export class CompanyUserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    userName: [],
    idCardNum: [],
    gender: [],
    phoneNum: [],
    email: [],
    deptName: [],
    postName: [],
    enable: [],
    remark: [],
    delFlag: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
  });

  constructor(protected companyUserService: CompanyUserService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyUser }) => {
      if (companyUser.id === undefined) {
        const today = dayjs().startOf('day');
        companyUser.createdDate = today;
        companyUser.lastModifiedDate = today;
      }

      this.updateForm(companyUser);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const companyUser = this.createFromForm();
    if (companyUser.id !== undefined) {
      this.subscribeToSaveResponse(this.companyUserService.update(companyUser));
    } else {
      this.subscribeToSaveResponse(this.companyUserService.create(companyUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyUser>>): void {
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

  protected updateForm(companyUser: ICompanyUser): void {
    this.editForm.patchValue({
      id: companyUser.id,
      userName: companyUser.userName,
      idCardNum: companyUser.idCardNum,
      gender: companyUser.gender,
      phoneNum: companyUser.phoneNum,
      email: companyUser.email,
      deptName: companyUser.deptName,
      postName: companyUser.postName,
      enable: companyUser.enable,
      remark: companyUser.remark,
      delFlag: companyUser.delFlag,
      createdBy: companyUser.createdBy,
      createdDate: companyUser.createdDate ? companyUser.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: companyUser.lastModifiedBy,
      lastModifiedDate: companyUser.lastModifiedDate ? companyUser.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ICompanyUser {
    return {
      ...new CompanyUser(),
      id: this.editForm.get(['id'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      idCardNum: this.editForm.get(['idCardNum'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      phoneNum: this.editForm.get(['phoneNum'])!.value,
      email: this.editForm.get(['email'])!.value,
      deptName: this.editForm.get(['deptName'])!.value,
      postName: this.editForm.get(['postName'])!.value,
      enable: this.editForm.get(['enable'])!.value,
      remark: this.editForm.get(['remark'])!.value,
      delFlag: this.editForm.get(['delFlag'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? dayjs(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
