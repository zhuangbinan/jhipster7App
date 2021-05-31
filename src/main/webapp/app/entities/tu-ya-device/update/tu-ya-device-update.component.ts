import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITuYaDevice, TuYaDevice } from '../tu-ya-device.model';
import { TuYaDeviceService } from '../service/tu-ya-device.service';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';
import { RoomAddrService } from 'app/entities/room-addr/service/room-addr.service';

@Component({
  selector: 'jhi-tu-ya-device-update',
  templateUrl: './tu-ya-device-update.component.html',
})
export class TuYaDeviceUpdateComponent implements OnInit {
  isSaving = false;

  roomAddrsSharedCollection: IRoomAddr[] = [];

  editForm = this.fb.group({
    id: [],
    deviceName: [],
    longCode: [null, []],
    tyDeviceId: [null, []],
    deviceType: [],
    cmdCode: [],
    createTime: [],
    enable: [],
    roomAddr: [],
  });

  constructor(
    protected tuYaDeviceService: TuYaDeviceService,
    protected roomAddrService: RoomAddrService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tuYaDevice }) => {
      if (tuYaDevice.id === undefined) {
        const today = dayjs().startOf('day');
        tuYaDevice.createTime = today;
      }

      this.updateForm(tuYaDevice);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tuYaDevice = this.createFromForm();
    if (tuYaDevice.id !== undefined) {
      this.subscribeToSaveResponse(this.tuYaDeviceService.update(tuYaDevice));
    } else {
      this.subscribeToSaveResponse(this.tuYaDeviceService.create(tuYaDevice));
    }
  }

  trackRoomAddrById(index: number, item: IRoomAddr): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITuYaDevice>>): void {
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

  protected updateForm(tuYaDevice: ITuYaDevice): void {
    this.editForm.patchValue({
      id: tuYaDevice.id,
      deviceName: tuYaDevice.deviceName,
      longCode: tuYaDevice.longCode,
      tyDeviceId: tuYaDevice.tyDeviceId,
      deviceType: tuYaDevice.deviceType,
      cmdCode: tuYaDevice.cmdCode,
      createTime: tuYaDevice.createTime ? tuYaDevice.createTime.format(DATE_TIME_FORMAT) : null,
      enable: tuYaDevice.enable,
      roomAddr: tuYaDevice.roomAddr,
    });

    this.roomAddrsSharedCollection = this.roomAddrService.addRoomAddrToCollectionIfMissing(
      this.roomAddrsSharedCollection,
      tuYaDevice.roomAddr
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

  protected createFromForm(): ITuYaDevice {
    return {
      ...new TuYaDevice(),
      id: this.editForm.get(['id'])!.value,
      deviceName: this.editForm.get(['deviceName'])!.value,
      longCode: this.editForm.get(['longCode'])!.value,
      tyDeviceId: this.editForm.get(['tyDeviceId'])!.value,
      deviceType: this.editForm.get(['deviceType'])!.value,
      cmdCode: this.editForm.get(['cmdCode'])!.value,
      createTime: this.editForm.get(['createTime'])!.value ? dayjs(this.editForm.get(['createTime'])!.value, DATE_TIME_FORMAT) : undefined,
      enable: this.editForm.get(['enable'])!.value,
      roomAddr: this.editForm.get(['roomAddr'])!.value,
    };
  }
}
