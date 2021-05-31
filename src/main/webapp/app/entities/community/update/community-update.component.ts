import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommunity, Community } from '../community.model';
import { CommunityService } from '../service/community.service';

@Component({
  selector: 'jhi-community-update',
  templateUrl: './community-update.component.html',
})
export class CommunityUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cname: [],
    managerName: [],
    address: [],
    tel: [],
    email: [],
    officeHours: [],
    description: [],
    source: [],
    parentId: [],
    ancestors: [],
    longCode: [],
    enable: [],
    delFlag: [],
    orderNum: [],
    lastModifyDate: [],
    lastModifyBy: [],
  });

  constructor(protected communityService: CommunityService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ community }) => {
      if (community.id === undefined) {
        const today = dayjs().startOf('day');
        community.lastModifyDate = today;
      }

      this.updateForm(community);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const community = this.createFromForm();
    if (community.id !== undefined) {
      this.subscribeToSaveResponse(this.communityService.update(community));
    } else {
      this.subscribeToSaveResponse(this.communityService.create(community));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommunity>>): void {
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

  protected updateForm(community: ICommunity): void {
    this.editForm.patchValue({
      id: community.id,
      cname: community.cname,
      managerName: community.managerName,
      address: community.address,
      tel: community.tel,
      email: community.email,
      officeHours: community.officeHours,
      description: community.description,
      source: community.source,
      parentId: community.parentId,
      ancestors: community.ancestors,
      longCode: community.longCode,
      enable: community.enable,
      delFlag: community.delFlag,
      orderNum: community.orderNum,
      lastModifyDate: community.lastModifyDate ? community.lastModifyDate.format(DATE_TIME_FORMAT) : null,
      lastModifyBy: community.lastModifyBy,
    });
  }

  protected createFromForm(): ICommunity {
    return {
      ...new Community(),
      id: this.editForm.get(['id'])!.value,
      cname: this.editForm.get(['cname'])!.value,
      managerName: this.editForm.get(['managerName'])!.value,
      address: this.editForm.get(['address'])!.value,
      tel: this.editForm.get(['tel'])!.value,
      email: this.editForm.get(['email'])!.value,
      officeHours: this.editForm.get(['officeHours'])!.value,
      description: this.editForm.get(['description'])!.value,
      source: this.editForm.get(['source'])!.value,
      parentId: this.editForm.get(['parentId'])!.value,
      ancestors: this.editForm.get(['ancestors'])!.value,
      longCode: this.editForm.get(['longCode'])!.value,
      enable: this.editForm.get(['enable'])!.value,
      delFlag: this.editForm.get(['delFlag'])!.value,
      orderNum: this.editForm.get(['orderNum'])!.value,
      lastModifyDate: this.editForm.get(['lastModifyDate'])!.value
        ? dayjs(this.editForm.get(['lastModifyDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifyBy: this.editForm.get(['lastModifyBy'])!.value,
    };
  }
}
