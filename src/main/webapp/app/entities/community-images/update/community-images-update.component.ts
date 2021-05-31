import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommunityImages, CommunityImages } from '../community-images.model';
import { CommunityImagesService } from '../service/community-images.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICommunityImageGroup } from 'app/entities/community-image-group/community-image-group.model';
import { CommunityImageGroupService } from 'app/entities/community-image-group/service/community-image-group.service';

@Component({
  selector: 'jhi-community-images-update',
  templateUrl: './community-images-update.component.html',
})
export class CommunityImagesUpdateComponent implements OnInit {
  isSaving = false;

  communityImageGroupsSharedCollection: ICommunityImageGroup[] = [];

  editForm = this.fb.group({
    id: [],
    imgContent: [],
    imgContentContentType: [],
    imgTitle: [],
    imgDesc: [],
    orderNum: [],
    lastModifyDate: [],
    lastModifyBy: [],
    communityImageGroup: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected communityImagesService: CommunityImagesService,
    protected communityImageGroupService: CommunityImageGroupService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communityImages }) => {
      if (communityImages.id === undefined) {
        const today = dayjs().startOf('day');
        communityImages.lastModifyDate = today;
      }

      this.updateForm(communityImages);

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
    const communityImages = this.createFromForm();
    if (communityImages.id !== undefined) {
      this.subscribeToSaveResponse(this.communityImagesService.update(communityImages));
    } else {
      this.subscribeToSaveResponse(this.communityImagesService.create(communityImages));
    }
  }

  trackCommunityImageGroupById(index: number, item: ICommunityImageGroup): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommunityImages>>): void {
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

  protected updateForm(communityImages: ICommunityImages): void {
    this.editForm.patchValue({
      id: communityImages.id,
      imgContent: communityImages.imgContent,
      imgContentContentType: communityImages.imgContentContentType,
      imgTitle: communityImages.imgTitle,
      imgDesc: communityImages.imgDesc,
      orderNum: communityImages.orderNum,
      lastModifyDate: communityImages.lastModifyDate ? communityImages.lastModifyDate.format(DATE_TIME_FORMAT) : null,
      lastModifyBy: communityImages.lastModifyBy,
      communityImageGroup: communityImages.communityImageGroup,
    });

    this.communityImageGroupsSharedCollection = this.communityImageGroupService.addCommunityImageGroupToCollectionIfMissing(
      this.communityImageGroupsSharedCollection,
      communityImages.communityImageGroup
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communityImageGroupService
      .query()
      .pipe(map((res: HttpResponse<ICommunityImageGroup[]>) => res.body ?? []))
      .pipe(
        map((communityImageGroups: ICommunityImageGroup[]) =>
          this.communityImageGroupService.addCommunityImageGroupToCollectionIfMissing(
            communityImageGroups,
            this.editForm.get('communityImageGroup')!.value
          )
        )
      )
      .subscribe((communityImageGroups: ICommunityImageGroup[]) => (this.communityImageGroupsSharedCollection = communityImageGroups));
  }

  protected createFromForm(): ICommunityImages {
    return {
      ...new CommunityImages(),
      id: this.editForm.get(['id'])!.value,
      imgContentContentType: this.editForm.get(['imgContentContentType'])!.value,
      imgContent: this.editForm.get(['imgContent'])!.value,
      imgTitle: this.editForm.get(['imgTitle'])!.value,
      imgDesc: this.editForm.get(['imgDesc'])!.value,
      orderNum: this.editForm.get(['orderNum'])!.value,
      lastModifyDate: this.editForm.get(['lastModifyDate'])!.value
        ? dayjs(this.editForm.get(['lastModifyDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifyBy: this.editForm.get(['lastModifyBy'])!.value,
      communityImageGroup: this.editForm.get(['communityImageGroup'])!.value,
    };
  }
}
