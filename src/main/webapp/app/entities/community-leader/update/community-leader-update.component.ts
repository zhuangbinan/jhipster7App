import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommunityLeader, CommunityLeader } from '../community-leader.model';
import { CommunityLeaderService } from '../service/community-leader.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-community-leader-update',
  templateUrl: './community-leader-update.component.html',
})
export class CommunityLeaderUpdateComponent implements OnInit {
  isSaving = false;

  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    avatar: [],
    avatarContentType: [],
    realName: [],
    tel: [],
    jobTitle: [],
    jobDesc: [],
    jobCareerDesc: [],
    enable: [],
    delFlag: [],
    orderNum: [],
    lastModifyDate: [],
    lastModifyBy: [],
    community: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected communityLeaderService: CommunityLeaderService,
    protected communityService: CommunityService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communityLeader }) => {
      if (communityLeader.id === undefined) {
        const today = dayjs().startOf('day');
        communityLeader.lastModifyDate = today;
      }

      this.updateForm(communityLeader);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('jhipster7App.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const communityLeader = this.createFromForm();
    if (communityLeader.id !== undefined) {
      this.subscribeToSaveResponse(this.communityLeaderService.update(communityLeader));
    } else {
      this.subscribeToSaveResponse(this.communityLeaderService.create(communityLeader));
    }
  }

  trackCommunityById(index: number, item: ICommunity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommunityLeader>>): void {
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

  protected updateForm(communityLeader: ICommunityLeader): void {
    this.editForm.patchValue({
      id: communityLeader.id,
      avatar: communityLeader.avatar,
      avatarContentType: communityLeader.avatarContentType,
      realName: communityLeader.realName,
      tel: communityLeader.tel,
      jobTitle: communityLeader.jobTitle,
      jobDesc: communityLeader.jobDesc,
      jobCareerDesc: communityLeader.jobCareerDesc,
      enable: communityLeader.enable,
      delFlag: communityLeader.delFlag,
      orderNum: communityLeader.orderNum,
      lastModifyDate: communityLeader.lastModifyDate ? communityLeader.lastModifyDate.format(DATE_TIME_FORMAT) : null,
      lastModifyBy: communityLeader.lastModifyBy,
      community: communityLeader.community,
    });

    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      communityLeader.community
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communityService
      .query()
      .pipe(map((res: HttpResponse<ICommunity[]>) => res.body ?? []))
      .pipe(
        map((communities: ICommunity[]) =>
          this.communityService.addCommunityToCollectionIfMissing(communities, this.editForm.get('community')!.value)
        )
      )
      .subscribe((communities: ICommunity[]) => (this.communitiesSharedCollection = communities));
  }

  protected createFromForm(): ICommunityLeader {
    return {
      ...new CommunityLeader(),
      id: this.editForm.get(['id'])!.value,
      avatarContentType: this.editForm.get(['avatarContentType'])!.value,
      avatar: this.editForm.get(['avatar'])!.value,
      realName: this.editForm.get(['realName'])!.value,
      tel: this.editForm.get(['tel'])!.value,
      jobTitle: this.editForm.get(['jobTitle'])!.value,
      jobDesc: this.editForm.get(['jobDesc'])!.value,
      jobCareerDesc: this.editForm.get(['jobCareerDesc'])!.value,
      enable: this.editForm.get(['enable'])!.value,
      delFlag: this.editForm.get(['delFlag'])!.value,
      orderNum: this.editForm.get(['orderNum'])!.value,
      lastModifyDate: this.editForm.get(['lastModifyDate'])!.value
        ? dayjs(this.editForm.get(['lastModifyDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifyBy: this.editForm.get(['lastModifyBy'])!.value,
      community: this.editForm.get(['community'])!.value,
    };
  }
}
