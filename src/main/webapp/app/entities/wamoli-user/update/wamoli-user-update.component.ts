import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWamoliUser, WamoliUser } from '../wamoli-user.model';
import { WamoliUserService } from '../service/wamoli-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';
import { RoomAddrService } from 'app/entities/room-addr/service/room-addr.service';
import { ICompanyDept } from 'app/entities/company-dept/company-dept.model';
import { CompanyDeptService } from 'app/entities/company-dept/service/company-dept.service';

@Component({
  selector: 'jhi-wamoli-user-update',
  templateUrl: './wamoli-user-update.component.html',
})
export class WamoliUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  roomAddrsSharedCollection: IRoomAddr[] = [];
  companyDeptsSharedCollection: ICompanyDept[] = [];

  editForm = this.fb.group({
    id: [],
    userName: [null, [Validators.required]],
    gender: [],
    phoneNum: [null, [Validators.required]],
    email: [],
    unitAddr: [],
    roomAddr: [],
    idCardNum: [],
    idCardType: [],
    userType: [],
    enable: [],
    isManager: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    user: [],
    roomAddrs: [],
    companyDepts: [],
  });

  constructor(
    protected wamoliUserService: WamoliUserService,
    protected userService: UserService,
    protected roomAddrService: RoomAddrService,
    protected companyDeptService: CompanyDeptService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wamoliUser }) => {
      if (wamoliUser.id === undefined) {
        const today = dayjs().startOf('day');
        wamoliUser.lastModifiedDate = today;
      }

      this.updateForm(wamoliUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wamoliUser = this.createFromForm();
    if (wamoliUser.id !== undefined) {
      this.subscribeToSaveResponse(this.wamoliUserService.update(wamoliUser));
    } else {
      this.subscribeToSaveResponse(this.wamoliUserService.create(wamoliUser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackRoomAddrById(index: number, item: IRoomAddr): number {
    return item.id!;
  }

  trackCompanyDeptById(index: number, item: ICompanyDept): number {
    return item.id!;
  }

  getSelectedRoomAddr(option: IRoomAddr, selectedVals?: IRoomAddr[]): IRoomAddr {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedCompanyDept(option: ICompanyDept, selectedVals?: ICompanyDept[]): ICompanyDept {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWamoliUser>>): void {
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

  protected updateForm(wamoliUser: IWamoliUser): void {
    this.editForm.patchValue({
      id: wamoliUser.id,
      userName: wamoliUser.userName,
      gender: wamoliUser.gender,
      phoneNum: wamoliUser.phoneNum,
      email: wamoliUser.email,
      unitAddr: wamoliUser.unitAddr,
      roomAddr: wamoliUser.roomAddr,
      idCardNum: wamoliUser.idCardNum,
      idCardType: wamoliUser.idCardType,
      userType: wamoliUser.userType,
      enable: wamoliUser.enable,
      isManager: wamoliUser.isManager,
      lastModifiedBy: wamoliUser.lastModifiedBy,
      lastModifiedDate: wamoliUser.lastModifiedDate ? wamoliUser.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      user: wamoliUser.user,
      roomAddrs: wamoliUser.roomAddrs,
      companyDepts: wamoliUser.companyDepts,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, wamoliUser.user);
    this.roomAddrsSharedCollection = this.roomAddrService.addRoomAddrToCollectionIfMissing(
      this.roomAddrsSharedCollection,
      ...(wamoliUser.roomAddrs ?? [])
    );
    this.companyDeptsSharedCollection = this.companyDeptService.addCompanyDeptToCollectionIfMissing(
      this.companyDeptsSharedCollection,
      ...(wamoliUser.companyDepts ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.roomAddrService
      .query()
      .pipe(map((res: HttpResponse<IRoomAddr[]>) => res.body ?? []))
      .pipe(
        map((roomAddrs: IRoomAddr[]) =>
          this.roomAddrService.addRoomAddrToCollectionIfMissing(roomAddrs, ...(this.editForm.get('roomAddrs')!.value ?? []))
        )
      )
      .subscribe((roomAddrs: IRoomAddr[]) => (this.roomAddrsSharedCollection = roomAddrs));

    this.companyDeptService
      .query()
      .pipe(map((res: HttpResponse<ICompanyDept[]>) => res.body ?? []))
      .pipe(
        map((companyDepts: ICompanyDept[]) =>
          this.companyDeptService.addCompanyDeptToCollectionIfMissing(companyDepts, ...(this.editForm.get('companyDepts')!.value ?? []))
        )
      )
      .subscribe((companyDepts: ICompanyDept[]) => (this.companyDeptsSharedCollection = companyDepts));
  }

  protected createFromForm(): IWamoliUser {
    return {
      ...new WamoliUser(),
      id: this.editForm.get(['id'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      phoneNum: this.editForm.get(['phoneNum'])!.value,
      email: this.editForm.get(['email'])!.value,
      unitAddr: this.editForm.get(['unitAddr'])!.value,
      roomAddr: this.editForm.get(['roomAddr'])!.value,
      idCardNum: this.editForm.get(['idCardNum'])!.value,
      idCardType: this.editForm.get(['idCardType'])!.value,
      userType: this.editForm.get(['userType'])!.value,
      enable: this.editForm.get(['enable'])!.value,
      isManager: this.editForm.get(['isManager'])!.value,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? dayjs(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
      roomAddrs: this.editForm.get(['roomAddrs'])!.value,
      companyDepts: this.editForm.get(['companyDepts'])!.value,
    };
  }
}
