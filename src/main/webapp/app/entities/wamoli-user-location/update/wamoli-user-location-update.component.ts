import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWamoliUserLocation, WamoliUserLocation } from '../wamoli-user-location.model';
import { WamoliUserLocationService } from '../service/wamoli-user-location.service';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';
import { WamoliUserService } from 'app/entities/wamoli-user/service/wamoli-user.service';

@Component({
  selector: 'jhi-wamoli-user-location-update',
  templateUrl: './wamoli-user-location-update.component.html',
})
export class WamoliUserLocationUpdateComponent implements OnInit {
  isSaving = false;

  wamoliUsersCollection: IWamoliUser[] = [];

  editForm = this.fb.group({
    id: [],
    state: [null, [Validators.required]],
    cardNum: [null, [Validators.required]],
    expireTime: [],
    delayTime: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    wamoliUser: [],
  });

  constructor(
    protected wamoliUserLocationService: WamoliUserLocationService,
    protected wamoliUserService: WamoliUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wamoliUserLocation }) => {
      if (wamoliUserLocation.id === undefined) {
        const today = dayjs().startOf('day');
        wamoliUserLocation.expireTime = today;
        wamoliUserLocation.lastModifiedDate = today;
      }

      this.updateForm(wamoliUserLocation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wamoliUserLocation = this.createFromForm();
    if (wamoliUserLocation.id !== undefined) {
      this.subscribeToSaveResponse(this.wamoliUserLocationService.update(wamoliUserLocation));
    } else {
      this.subscribeToSaveResponse(this.wamoliUserLocationService.create(wamoliUserLocation));
    }
  }

  trackWamoliUserById(index: number, item: IWamoliUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWamoliUserLocation>>): void {
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

  protected updateForm(wamoliUserLocation: IWamoliUserLocation): void {
    this.editForm.patchValue({
      id: wamoliUserLocation.id,
      state: wamoliUserLocation.state,
      cardNum: wamoliUserLocation.cardNum,
      expireTime: wamoliUserLocation.expireTime ? wamoliUserLocation.expireTime.format(DATE_TIME_FORMAT) : null,
      delayTime: wamoliUserLocation.delayTime,
      lastModifiedBy: wamoliUserLocation.lastModifiedBy,
      lastModifiedDate: wamoliUserLocation.lastModifiedDate ? wamoliUserLocation.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      wamoliUser: wamoliUserLocation.wamoliUser,
    });

    this.wamoliUsersCollection = this.wamoliUserService.addWamoliUserToCollectionIfMissing(
      this.wamoliUsersCollection,
      wamoliUserLocation.wamoliUser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.wamoliUserService
      .query({ 'wamoliUserLocationId.specified': 'false' })
      .pipe(map((res: HttpResponse<IWamoliUser[]>) => res.body ?? []))
      .pipe(
        map((wamoliUsers: IWamoliUser[]) =>
          this.wamoliUserService.addWamoliUserToCollectionIfMissing(wamoliUsers, this.editForm.get('wamoliUser')!.value)
        )
      )
      .subscribe((wamoliUsers: IWamoliUser[]) => (this.wamoliUsersCollection = wamoliUsers));
  }

  protected createFromForm(): IWamoliUserLocation {
    return {
      ...new WamoliUserLocation(),
      id: this.editForm.get(['id'])!.value,
      state: this.editForm.get(['state'])!.value,
      cardNum: this.editForm.get(['cardNum'])!.value,
      expireTime: this.editForm.get(['expireTime'])!.value ? dayjs(this.editForm.get(['expireTime'])!.value, DATE_TIME_FORMAT) : undefined,
      delayTime: this.editForm.get(['delayTime'])!.value,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? dayjs(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      wamoliUser: this.editForm.get(['wamoliUser'])!.value,
    };
  }
}
