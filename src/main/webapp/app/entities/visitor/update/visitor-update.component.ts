import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVisitor, Visitor } from '../visitor.model';
import { VisitorService } from '../service/visitor.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';
import { RoomAddrService } from 'app/entities/room-addr/service/room-addr.service';

@Component({
  selector: 'jhi-visitor-update',
  templateUrl: './visitor-update.component.html',
})
export class VisitorUpdateComponent implements OnInit {
  isSaving = false;

  roomAddrsSharedCollection: IRoomAddr[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    phoneNum: [null, [Validators.required]],
    carPlateNum: [],
    startTime: [null, [Validators.required]],
    endTime: [null, [Validators.required]],
    passwd: [],
    faceImage: [null, []],
    faceImageContentType: [],
    whichEntrance: [],
    roomAddr: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected visitorService: VisitorService,
    protected roomAddrService: RoomAddrService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visitor }) => {
      if (visitor.id === undefined) {
        const today = dayjs().startOf('day');
        visitor.startTime = today;
        visitor.endTime = today;
      }

      this.updateForm(visitor);

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
    const visitor = this.createFromForm();
    if (visitor.id !== undefined) {
      this.subscribeToSaveResponse(this.visitorService.update(visitor));
    } else {
      this.subscribeToSaveResponse(this.visitorService.create(visitor));
    }
  }

  trackRoomAddrById(index: number, item: IRoomAddr): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisitor>>): void {
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

  protected updateForm(visitor: IVisitor): void {
    this.editForm.patchValue({
      id: visitor.id,
      name: visitor.name,
      phoneNum: visitor.phoneNum,
      carPlateNum: visitor.carPlateNum,
      startTime: visitor.startTime ? visitor.startTime.format(DATE_TIME_FORMAT) : null,
      endTime: visitor.endTime ? visitor.endTime.format(DATE_TIME_FORMAT) : null,
      passwd: visitor.passwd,
      faceImage: visitor.faceImage,
      faceImageContentType: visitor.faceImageContentType,
      whichEntrance: visitor.whichEntrance,
      roomAddr: visitor.roomAddr,
    });

    this.roomAddrsSharedCollection = this.roomAddrService.addRoomAddrToCollectionIfMissing(
      this.roomAddrsSharedCollection,
      visitor.roomAddr
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roomAddrService
      .query()
      .pipe(map((res: HttpResponse<IRoomAddr[]>) => res.body ?? []))
      .pipe(
        map((roomAddrs: IRoomAddr[]) =>
          this.roomAddrService.addRoomAddrToCollectionIfMissing(roomAddrs, this.editForm.get('roomAddr')!.value)
        )
      )
      .subscribe((roomAddrs: IRoomAddr[]) => (this.roomAddrsSharedCollection = roomAddrs));
  }

  protected createFromForm(): IVisitor {
    return {
      ...new Visitor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      phoneNum: this.editForm.get(['phoneNum'])!.value,
      carPlateNum: this.editForm.get(['carPlateNum'])!.value,
      startTime: this.editForm.get(['startTime'])!.value ? dayjs(this.editForm.get(['startTime'])!.value, DATE_TIME_FORMAT) : undefined,
      endTime: this.editForm.get(['endTime'])!.value ? dayjs(this.editForm.get(['endTime'])!.value, DATE_TIME_FORMAT) : undefined,
      passwd: this.editForm.get(['passwd'])!.value,
      faceImageContentType: this.editForm.get(['faceImageContentType'])!.value,
      faceImage: this.editForm.get(['faceImage'])!.value,
      whichEntrance: this.editForm.get(['whichEntrance'])!.value,
      roomAddr: this.editForm.get(['roomAddr'])!.value,
    };
  }
}
