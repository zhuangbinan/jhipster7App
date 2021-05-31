import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRoomAddr, RoomAddr } from '../room-addr.model';
import { RoomAddrService } from '../service/room-addr.service';
import { IBuildings } from 'app/entities/buildings/buildings.model';
import { BuildingsService } from 'app/entities/buildings/service/buildings.service';

@Component({
  selector: 'jhi-room-addr-update',
  templateUrl: './room-addr-update.component.html',
})
export class RoomAddrUpdateComponent implements OnInit {
  isSaving = false;

  buildingsSharedCollection: IBuildings[] = [];

  editForm = this.fb.group({
    id: [],
    roomNum: [null, [Validators.required]],
    unit: [],
    roomType: [],
    roomArea: [],
    used: [],
    autoControl: [],
    longCode: [null, [Validators.required]],
    buildings: [],
  });

  constructor(
    protected roomAddrService: RoomAddrService,
    protected buildingsService: BuildingsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomAddr }) => {
      this.updateForm(roomAddr);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roomAddr = this.createFromForm();
    if (roomAddr.id !== undefined) {
      this.subscribeToSaveResponse(this.roomAddrService.update(roomAddr));
    } else {
      this.subscribeToSaveResponse(this.roomAddrService.create(roomAddr));
    }
  }

  trackBuildingsById(index: number, item: IBuildings): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomAddr>>): void {
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

  protected updateForm(roomAddr: IRoomAddr): void {
    this.editForm.patchValue({
      id: roomAddr.id,
      roomNum: roomAddr.roomNum,
      unit: roomAddr.unit,
      roomType: roomAddr.roomType,
      roomArea: roomAddr.roomArea,
      used: roomAddr.used,
      autoControl: roomAddr.autoControl,
      longCode: roomAddr.longCode,
      buildings: roomAddr.buildings,
    });

    this.buildingsSharedCollection = this.buildingsService.addBuildingsToCollectionIfMissing(
      this.buildingsSharedCollection,
      roomAddr.buildings
    );
  }

  protected loadRelationshipsOptions(): void {
    this.buildingsService
      .query()
      .pipe(map((res: HttpResponse<IBuildings[]>) => res.body ?? []))
      .pipe(
        map((buildings: IBuildings[]) =>
          this.buildingsService.addBuildingsToCollectionIfMissing(buildings, this.editForm.get('buildings')!.value)
        )
      )
      .subscribe((buildings: IBuildings[]) => (this.buildingsSharedCollection = buildings));
  }

  protected createFromForm(): IRoomAddr {
    return {
      ...new RoomAddr(),
      id: this.editForm.get(['id'])!.value,
      roomNum: this.editForm.get(['roomNum'])!.value,
      unit: this.editForm.get(['unit'])!.value,
      roomType: this.editForm.get(['roomType'])!.value,
      roomArea: this.editForm.get(['roomArea'])!.value,
      used: this.editForm.get(['used'])!.value,
      autoControl: this.editForm.get(['autoControl'])!.value,
      longCode: this.editForm.get(['longCode'])!.value,
      buildings: this.editForm.get(['buildings'])!.value,
    };
  }
}
