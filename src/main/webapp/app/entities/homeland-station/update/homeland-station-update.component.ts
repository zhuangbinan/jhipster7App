import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IHomelandStation, HomelandStation } from '../homeland-station.model';
import { HomelandStationService } from '../service/homeland-station.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'jhi-homeland-station-update',
  templateUrl: './homeland-station-update.component.html',
})
export class HomelandStationUpdateComponent implements OnInit {
  isSaving = false;

  communitiesSharedCollection: ICommunity[] = [];
  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    longCode: [null, [Validators.required]],
    address: [],
    livingPopulation: [],
    buildingCount: [],
    entranceCount: [],
    logo: [null, []],
    logoContentType: [],
    longitude: [null, [Validators.required, Validators.maxLength(32)]],
    latitude: [null, [Validators.required, Validators.maxLength(32)]],
    community: [],
    company: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected homelandStationService: HomelandStationService,
    protected communityService: CommunityService,
    protected companyService: CompanyService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ homelandStation }) => {
      this.updateForm(homelandStation);

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
    const homelandStation = this.createFromForm();
    if (homelandStation.id !== undefined) {
      this.subscribeToSaveResponse(this.homelandStationService.update(homelandStation));
    } else {
      this.subscribeToSaveResponse(this.homelandStationService.create(homelandStation));
    }
  }

  trackCommunityById(index: number, item: ICommunity): number {
    return item.id!;
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHomelandStation>>): void {
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

  protected updateForm(homelandStation: IHomelandStation): void {
    this.editForm.patchValue({
      id: homelandStation.id,
      name: homelandStation.name,
      longCode: homelandStation.longCode,
      address: homelandStation.address,
      livingPopulation: homelandStation.livingPopulation,
      buildingCount: homelandStation.buildingCount,
      entranceCount: homelandStation.entranceCount,
      logo: homelandStation.logo,
      logoContentType: homelandStation.logoContentType,
      longitude: homelandStation.longitude,
      latitude: homelandStation.latitude,
      community: homelandStation.community,
      company: homelandStation.company,
    });

    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      homelandStation.community
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(
      this.companiesSharedCollection,
      homelandStation.company
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

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }

  protected createFromForm(): IHomelandStation {
    return {
      ...new HomelandStation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      longCode: this.editForm.get(['longCode'])!.value,
      address: this.editForm.get(['address'])!.value,
      livingPopulation: this.editForm.get(['livingPopulation'])!.value,
      buildingCount: this.editForm.get(['buildingCount'])!.value,
      entranceCount: this.editForm.get(['entranceCount'])!.value,
      logoContentType: this.editForm.get(['logoContentType'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      community: this.editForm.get(['community'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
