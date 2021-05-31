import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommunityNotice, CommunityNotice } from '../community-notice.model';
import { CommunityNoticeService } from '../service/community-notice.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-community-notice-update',
  templateUrl: './community-notice-update.component.html',
})
export class CommunityNoticeUpdateComponent implements OnInit {
  isSaving = false;

  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    img1: [],
    img1ContentType: [],
    img1Title: [],
    content1: [],
    img2: [],
    img2ContentType: [],
    img2Title: [],
    content2: [],
    img3: [],
    img3ContentType: [],
    img3Title: [],
    content3: [],
    img4: [],
    img4ContentType: [],
    img4Title: [],
    content4: [],
    img5: [],
    img5ContentType: [],
    img5Title: [],
    content5: [],
    tail: [],
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
    protected communityNoticeService: CommunityNoticeService,
    protected communityService: CommunityService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communityNotice }) => {
      if (communityNotice.id === undefined) {
        const today = dayjs().startOf('day');
        communityNotice.lastModifyDate = today;
      }

      this.updateForm(communityNotice);

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
    const communityNotice = this.createFromForm();
    if (communityNotice.id !== undefined) {
      this.subscribeToSaveResponse(this.communityNoticeService.update(communityNotice));
    } else {
      this.subscribeToSaveResponse(this.communityNoticeService.create(communityNotice));
    }
  }

  trackCommunityById(index: number, item: ICommunity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommunityNotice>>): void {
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

  protected updateForm(communityNotice: ICommunityNotice): void {
    this.editForm.patchValue({
      id: communityNotice.id,
      title: communityNotice.title,
      img1: communityNotice.img1,
      img1ContentType: communityNotice.img1ContentType,
      img1Title: communityNotice.img1Title,
      content1: communityNotice.content1,
      img2: communityNotice.img2,
      img2ContentType: communityNotice.img2ContentType,
      img2Title: communityNotice.img2Title,
      content2: communityNotice.content2,
      img3: communityNotice.img3,
      img3ContentType: communityNotice.img3ContentType,
      img3Title: communityNotice.img3Title,
      content3: communityNotice.content3,
      img4: communityNotice.img4,
      img4ContentType: communityNotice.img4ContentType,
      img4Title: communityNotice.img4Title,
      content4: communityNotice.content4,
      img5: communityNotice.img5,
      img5ContentType: communityNotice.img5ContentType,
      img5Title: communityNotice.img5Title,
      content5: communityNotice.content5,
      tail: communityNotice.tail,
      enable: communityNotice.enable,
      delFlag: communityNotice.delFlag,
      orderNum: communityNotice.orderNum,
      lastModifyDate: communityNotice.lastModifyDate ? communityNotice.lastModifyDate.format(DATE_TIME_FORMAT) : null,
      lastModifyBy: communityNotice.lastModifyBy,
      community: communityNotice.community,
    });

    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      communityNotice.community
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

  protected createFromForm(): ICommunityNotice {
    return {
      ...new CommunityNotice(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      img1ContentType: this.editForm.get(['img1ContentType'])!.value,
      img1: this.editForm.get(['img1'])!.value,
      img1Title: this.editForm.get(['img1Title'])!.value,
      content1: this.editForm.get(['content1'])!.value,
      img2ContentType: this.editForm.get(['img2ContentType'])!.value,
      img2: this.editForm.get(['img2'])!.value,
      img2Title: this.editForm.get(['img2Title'])!.value,
      content2: this.editForm.get(['content2'])!.value,
      img3ContentType: this.editForm.get(['img3ContentType'])!.value,
      img3: this.editForm.get(['img3'])!.value,
      img3Title: this.editForm.get(['img3Title'])!.value,
      content3: this.editForm.get(['content3'])!.value,
      img4ContentType: this.editForm.get(['img4ContentType'])!.value,
      img4: this.editForm.get(['img4'])!.value,
      img4Title: this.editForm.get(['img4Title'])!.value,
      content4: this.editForm.get(['content4'])!.value,
      img5ContentType: this.editForm.get(['img5ContentType'])!.value,
      img5: this.editForm.get(['img5'])!.value,
      img5Title: this.editForm.get(['img5Title'])!.value,
      content5: this.editForm.get(['content5'])!.value,
      tail: this.editForm.get(['tail'])!.value,
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
