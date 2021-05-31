import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITuYaCmd, TuYaCmd } from '../tu-ya-cmd.model';
import { TuYaCmdService } from '../service/tu-ya-cmd.service';
import { ITuYaDevice } from 'app/entities/tu-ya-device/tu-ya-device.model';
import { TuYaDeviceService } from 'app/entities/tu-ya-device/service/tu-ya-device.service';

@Component({
  selector: 'jhi-tu-ya-cmd-update',
  templateUrl: './tu-ya-cmd-update.component.html',
})
export class TuYaCmdUpdateComponent implements OnInit {
  isSaving = false;

  tuYaDevicesSharedCollection: ITuYaDevice[] = [];

  editForm = this.fb.group({
    id: [],
    cmdName: [],
    cmdCode: [],
    value: [],
    cmdType: [],
    createTime: [],
    enable: [],
    tuYaDevice: [],
  });

  constructor(
    protected tuYaCmdService: TuYaCmdService,
    protected tuYaDeviceService: TuYaDeviceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tuYaCmd }) => {
      if (tuYaCmd.id === undefined) {
        const today = dayjs().startOf('day');
        tuYaCmd.createTime = today;
      }

      this.updateForm(tuYaCmd);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tuYaCmd = this.createFromForm();
    if (tuYaCmd.id !== undefined) {
      this.subscribeToSaveResponse(this.tuYaCmdService.update(tuYaCmd));
    } else {
      this.subscribeToSaveResponse(this.tuYaCmdService.create(tuYaCmd));
    }
  }

  trackTuYaDeviceById(index: number, item: ITuYaDevice): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITuYaCmd>>): void {
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

  protected updateForm(tuYaCmd: ITuYaCmd): void {
    this.editForm.patchValue({
      id: tuYaCmd.id,
      cmdName: tuYaCmd.cmdName,
      cmdCode: tuYaCmd.cmdCode,
      value: tuYaCmd.value,
      cmdType: tuYaCmd.cmdType,
      createTime: tuYaCmd.createTime ? tuYaCmd.createTime.format(DATE_TIME_FORMAT) : null,
      enable: tuYaCmd.enable,
      tuYaDevice: tuYaCmd.tuYaDevice,
    });

    this.tuYaDevicesSharedCollection = this.tuYaDeviceService.addTuYaDeviceToCollectionIfMissing(
      this.tuYaDevicesSharedCollection,
      tuYaCmd.tuYaDevice
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tuYaDeviceService
      .query()
      .pipe(map((res: HttpResponse<ITuYaDevice[]>) => res.body ?? []))
      .pipe(
        map((tuYaDevices: ITuYaDevice[]) =>
          this.tuYaDeviceService.addTuYaDeviceToCollectionIfMissing(tuYaDevices, this.editForm.get('tuYaDevice')!.value)
        )
      )
      .subscribe((tuYaDevices: ITuYaDevice[]) => (this.tuYaDevicesSharedCollection = tuYaDevices));
  }

  protected createFromForm(): ITuYaCmd {
    return {
      ...new TuYaCmd(),
      id: this.editForm.get(['id'])!.value,
      cmdName: this.editForm.get(['cmdName'])!.value,
      cmdCode: this.editForm.get(['cmdCode'])!.value,
      value: this.editForm.get(['value'])!.value,
      cmdType: this.editForm.get(['cmdType'])!.value,
      createTime: this.editForm.get(['createTime'])!.value ? dayjs(this.editForm.get(['createTime'])!.value, DATE_TIME_FORMAT) : undefined,
      enable: this.editForm.get(['enable'])!.value,
      tuYaDevice: this.editForm.get(['tuYaDevice'])!.value,
    };
  }
}
