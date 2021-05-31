import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompanyDept, CompanyDept } from '../company-dept.model';
import { CompanyDeptService } from '../service/company-dept.service';

@Component({
  selector: 'jhi-company-dept-update',
  templateUrl: './company-dept-update.component.html',
})
export class CompanyDeptUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    parentId: [],
    ancestors: [],
    deptName: [],
    orderNum: [],
    leaderName: [],
    tel: [],
    email: [],
    enable: [],
    delFlag: [],
    createBy: [],
    createDate: [],
    lastModifyBy: [],
    lastModifyDate: [],
  });

  constructor(protected companyDeptService: CompanyDeptService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyDept }) => {
      if (companyDept.id === undefined) {
        const today = dayjs().startOf('day');
        companyDept.createDate = today;
        companyDept.lastModifyDate = today;
      }

      this.updateForm(companyDept);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const companyDept = this.createFromForm();
    if (companyDept.id !== undefined) {
      this.subscribeToSaveResponse(this.companyDeptService.update(companyDept));
    } else {
      this.subscribeToSaveResponse(this.companyDeptService.create(companyDept));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyDept>>): void {
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

  protected updateForm(companyDept: ICompanyDept): void {
    this.editForm.patchValue({
      id: companyDept.id,
      parentId: companyDept.parentId,
      ancestors: companyDept.ancestors,
      deptName: companyDept.deptName,
      orderNum: companyDept.orderNum,
      leaderName: companyDept.leaderName,
      tel: companyDept.tel,
      email: companyDept.email,
      enable: companyDept.enable,
      delFlag: companyDept.delFlag,
      createBy: companyDept.createBy,
      createDate: companyDept.createDate ? companyDept.createDate.format(DATE_TIME_FORMAT) : null,
      lastModifyBy: companyDept.lastModifyBy,
      lastModifyDate: companyDept.lastModifyDate ? companyDept.lastModifyDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ICompanyDept {
    return {
      ...new CompanyDept(),
      id: this.editForm.get(['id'])!.value,
      parentId: this.editForm.get(['parentId'])!.value,
      ancestors: this.editForm.get(['ancestors'])!.value,
      deptName: this.editForm.get(['deptName'])!.value,
      orderNum: this.editForm.get(['orderNum'])!.value,
      leaderName: this.editForm.get(['leaderName'])!.value,
      tel: this.editForm.get(['tel'])!.value,
      email: this.editForm.get(['email'])!.value,
      enable: this.editForm.get(['enable'])!.value,
      delFlag: this.editForm.get(['delFlag'])!.value,
      createBy: this.editForm.get(['createBy'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      lastModifyBy: this.editForm.get(['lastModifyBy'])!.value,
      lastModifyDate: this.editForm.get(['lastModifyDate'])!.value
        ? dayjs(this.editForm.get(['lastModifyDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
