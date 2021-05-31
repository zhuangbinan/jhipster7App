import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWamoliFaceLibrary, WamoliFaceLibrary } from '../wamoli-face-library.model';
import { WamoliFaceLibraryService } from '../service/wamoli-face-library.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';
import { WamoliUserService } from 'app/entities/wamoli-user/service/wamoli-user.service';

@Component({
  selector: 'jhi-wamoli-face-library-update',
  templateUrl: './wamoli-face-library-update.component.html',
})
export class WamoliFaceLibraryUpdateComponent implements OnInit {
  isSaving = false;

  wamoliUsersCollection: IWamoliUser[] = [];

  editForm = this.fb.group({
    id: [],
    content: [null, [Validators.required]],
    wamoliUser: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected wamoliFaceLibraryService: WamoliFaceLibraryService,
    protected wamoliUserService: WamoliUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wamoliFaceLibrary }) => {
      this.updateForm(wamoliFaceLibrary);

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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wamoliFaceLibrary = this.createFromForm();
    if (wamoliFaceLibrary.id !== undefined) {
      this.subscribeToSaveResponse(this.wamoliFaceLibraryService.update(wamoliFaceLibrary));
    } else {
      this.subscribeToSaveResponse(this.wamoliFaceLibraryService.create(wamoliFaceLibrary));
    }
  }

  trackWamoliUserById(index: number, item: IWamoliUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWamoliFaceLibrary>>): void {
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

  protected updateForm(wamoliFaceLibrary: IWamoliFaceLibrary): void {
    this.editForm.patchValue({
      id: wamoliFaceLibrary.id,
      content: wamoliFaceLibrary.content,
      wamoliUser: wamoliFaceLibrary.wamoliUser,
    });

    this.wamoliUsersCollection = this.wamoliUserService.addWamoliUserToCollectionIfMissing(
      this.wamoliUsersCollection,
      wamoliFaceLibrary.wamoliUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.wamoliUserService
      .query({ 'wamoliFaceLibraryId.specified': 'false' })
      .pipe(map((res: HttpResponse<IWamoliUser[]>) => res.body ?? []))
      .pipe(
        map((wamoliUsers: IWamoliUser[]) =>
          this.wamoliUserService.addWamoliUserToCollectionIfMissing(wamoliUsers, this.editForm.get('wamoliUser')!.value)
        )
      )
      .subscribe((wamoliUsers: IWamoliUser[]) => (this.wamoliUsersCollection = wamoliUsers));
  }

  protected createFromForm(): IWamoliFaceLibrary {
    return {
      ...new WamoliFaceLibrary(),
      id: this.editForm.get(['id'])!.value,
      content: this.editForm.get(['content'])!.value,
      wamoliUser: this.editForm.get(['wamoliUser'])!.value,
    };
  }
}
