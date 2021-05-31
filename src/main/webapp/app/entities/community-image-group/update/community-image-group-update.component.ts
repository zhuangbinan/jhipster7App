import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommunityImageGroup, CommunityImageGroup } from '../community-image-group.model';
import { CommunityImageGroupService } from '../service/community-image-group.service';

@Component({
  selector: 'jhi-community-image-group-update',
  templateUrl: './community-image-group-update.component.html',
})
export class CommunityImageGroupUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    imgGroupName: [null, []],
    orderNum: [],
    lastModifyDate: [],
    lastModifyBy: [],
  });

  constructor(
    protected communityImageGroupService: CommunityImageGroupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communityImageGroup }) => {
      if (communityImageGroup.id === undefined) {
        const today = dayjs().startOf('day');
        communityImageGroup.lastModifyDate = today;
      }

      this.updateForm(communityImageGroup);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const communityImageGroup = this.createFromForm();
    if (communityImageGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.communityImageGroupService.update(communityImageGroup));
    } else {
      this.subscribeToSaveResponse(this.communityImageGroupService.create(communityImageGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommunityImageGroup>>): void {
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

  protected updateForm(communityImageGroup: ICommunityImageGroup): void {
    this.editForm.patchValue({
      id: communityImageGroup.id,
      imgGroupName: communityImageGroup.imgGroupName,
      orderNum: communityImageGroup.orderNum,
      lastModifyDate: communityImageGroup.lastModifyDate ? communityImageGroup.lastModifyDate.format(DATE_TIME_FORMAT) : null,
      lastModifyBy: communityImageGroup.lastModifyBy,
    });
  }

  protected createFromForm(): ICommunityImageGroup {
    return {
      ...new CommunityImageGroup(),
      id: this.editForm.get(['id'])!.value,
      imgGroupName: this.editForm.get(['imgGroupName'])!.value,
      orderNum: this.editForm.get(['orderNum'])!.value,
      lastModifyDate: this.editForm.get(['lastModifyDate'])!.value
        ? dayjs(this.editForm.get(['lastModifyDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifyBy: this.editForm.get(['lastModifyBy'])!.value,
    };
  }
}
