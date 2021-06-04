import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompanyPost, CompanyPost } from '../company-post.model';
import { CompanyPostService } from '../service/company-post.service';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';
import { WamoliUserService } from 'app/entities/wamoli-user/service/wamoli-user.service';

@Component({
  selector: 'jhi-company-post-update',
  templateUrl: './company-post-update.component.html',
})
export class CompanyPostUpdateComponent implements OnInit {
  isSaving = false;

  wamoliUsersSharedCollection: IWamoliUser[] = [];

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
    wamoliUsers: [],
  });

  constructor(
    protected companyPostService: CompanyPostService,
    protected wamoliUserService: WamoliUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyPost }) => {
      if (companyPost.id === undefined) {
        const today = dayjs().startOf('day');
        companyPost.createDate = today;
        companyPost.lastModifyDate = today;
      }

      this.updateForm(companyPost);

      this.loadRelationshipsOptions();
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

  trackWamoliUserById(index: number, item: IWamoliUser): number {
    return item.id!;
  }

  getSelectedWamoliUser(option: IWamoliUser, selectedVals?: IWamoliUser[]): IWamoliUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
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
      wamoliUsers: companyPost.wamoliUsers,
    });

    this.wamoliUsersSharedCollection = this.wamoliUserService.addWamoliUserToCollectionIfMissing(
      this.wamoliUsersSharedCollection,
      ...(companyPost.wamoliUsers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.wamoliUserService
      .query()
      .pipe(map((res: HttpResponse<IWamoliUser[]>) => res.body ?? []))
      .pipe(
        map((wamoliUsers: IWamoliUser[]) =>
          this.wamoliUserService.addWamoliUserToCollectionIfMissing(wamoliUsers, ...(this.editForm.get('wamoliUsers')!.value ?? []))
        )
      )
      .subscribe((wamoliUsers: IWamoliUser[]) => (this.wamoliUsersSharedCollection = wamoliUsers));
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
      wamoliUsers: this.editForm.get(['wamoliUsers'])!.value,
    };
  }
}
