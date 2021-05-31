import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITest01, Test01 } from '../test-01.model';
import { Test01Service } from '../service/test-01.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-test-01-update',
  templateUrl: './test-01-update.component.html',
})
export class Test01UpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    jobCareerDesc: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected test01Service: Test01Service,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ test01 }) => {
      this.updateForm(test01);
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
    const test01 = this.createFromForm();
    if (test01.id !== undefined) {
      this.subscribeToSaveResponse(this.test01Service.update(test01));
    } else {
      this.subscribeToSaveResponse(this.test01Service.create(test01));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITest01>>): void {
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

  protected updateForm(test01: ITest01): void {
    this.editForm.patchValue({
      id: test01.id,
      jobCareerDesc: test01.jobCareerDesc,
    });
  }

  protected createFromForm(): ITest01 {
    return {
      ...new Test01(),
      id: this.editForm.get(['id'])!.value,
      jobCareerDesc: this.editForm.get(['jobCareerDesc'])!.value,
    };
  }
}
