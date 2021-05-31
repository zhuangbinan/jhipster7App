import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBuildings, Buildings } from '../buildings.model';
import { BuildingsService } from '../service/buildings.service';
import { IHomelandStation } from 'app/entities/homeland-station/homeland-station.model';
import { HomelandStationService } from 'app/entities/homeland-station/service/homeland-station.service';

@Component({
  selector: 'jhi-buildings-update',
  templateUrl: './buildings-update.component.html',
})
export class BuildingsUpdateComponent implements OnInit {
  isSaving = false;

  homelandStationsSharedCollection: IHomelandStation[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    longCode: [null, [Validators.required]],
    floorCount: [],
    unites: [],
    longitude: [null, [Validators.required, Validators.maxLength(32)]],
    latitude: [null, [Validators.required, Validators.maxLength(32)]],
    enable: [],
    homelandStation: [],
  });

  constructor(
    protected buildingsService: BuildingsService,
    protected homelandStationService: HomelandStationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ buildings }) => {
      this.updateForm(buildings);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const buildings = this.createFromForm();
    if (buildings.id !== undefined) {
      this.subscribeToSaveResponse(this.buildingsService.update(buildings));
    } else {
      this.subscribeToSaveResponse(this.buildingsService.create(buildings));
    }
  }

  trackHomelandStationById(index: number, item: IHomelandStation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBuildings>>): void {
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

  protected updateForm(buildings: IBuildings): void {
    this.editForm.patchValue({
      id: buildings.id,
      name: buildings.name,
      longCode: buildings.longCode,
      floorCount: buildings.floorCount,
      unites: buildings.unites,
      longitude: buildings.longitude,
      latitude: buildings.latitude,
      enable: buildings.enable,
      homelandStation: buildings.homelandStation,
    });

    this.homelandStationsSharedCollection = this.homelandStationService.addHomelandStationToCollectionIfMissing(
      this.homelandStationsSharedCollection,
      buildings.homelandStation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.homelandStationService
      .query()
      .pipe(map((res: HttpResponse<IHomelandStation[]>) => res.body ?? []))
      .pipe(
        map((homelandStations: IHomelandStation[]) =>
          this.homelandStationService.addHomelandStationToCollectionIfMissing(homelandStations, this.editForm.get('homelandStation')!.value)
        )
      )
      .subscribe((homelandStations: IHomelandStation[]) => (this.homelandStationsSharedCollection = homelandStations));
  }

  protected createFromForm(): IBuildings {
    return {
      ...new Buildings(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      longCode: this.editForm.get(['longCode'])!.value,
      floorCount: this.editForm.get(['floorCount'])!.value,
      unites: this.editForm.get(['unites'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      enable: this.editForm.get(['enable'])!.value,
      homelandStation: this.editForm.get(['homelandStation'])!.value,
    };
  }
}
