jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WamoliUserService } from '../service/wamoli-user.service';
import { IWamoliUser, WamoliUser } from '../wamoli-user.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';
import { RoomAddrService } from 'app/entities/room-addr/service/room-addr.service';

import { WamoliUserUpdateComponent } from './wamoli-user-update.component';

describe('Component Tests', () => {
  describe('WamoliUser Management Update Component', () => {
    let comp: WamoliUserUpdateComponent;
    let fixture: ComponentFixture<WamoliUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let wamoliUserService: WamoliUserService;
    let userService: UserService;
    let roomAddrService: RoomAddrService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WamoliUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WamoliUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WamoliUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      wamoliUserService = TestBed.inject(WamoliUserService);
      userService = TestBed.inject(UserService);
      roomAddrService = TestBed.inject(RoomAddrService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const wamoliUser: IWamoliUser = { id: 456 };
        const user: IUser = { id: 27699 };
        wamoliUser.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ wamoliUser });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call RoomAddr query and add missing value', () => {
        const wamoliUser: IWamoliUser = { id: 456 };
        const roomAddrs: IRoomAddr[] = [{ id: 90655 }];
        wamoliUser.roomAddrs = roomAddrs;

        const roomAddrCollection: IRoomAddr[] = [{ id: 90201 }];
        spyOn(roomAddrService, 'query').and.returnValue(of(new HttpResponse({ body: roomAddrCollection })));
        const additionalRoomAddrs = [...roomAddrs];
        const expectedCollection: IRoomAddr[] = [...additionalRoomAddrs, ...roomAddrCollection];
        spyOn(roomAddrService, 'addRoomAddrToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ wamoliUser });
        comp.ngOnInit();

        expect(roomAddrService.query).toHaveBeenCalled();
        expect(roomAddrService.addRoomAddrToCollectionIfMissing).toHaveBeenCalledWith(roomAddrCollection, ...additionalRoomAddrs);
        expect(comp.roomAddrsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const wamoliUser: IWamoliUser = { id: 456 };
        const user: IUser = { id: 47918 };
        wamoliUser.user = user;
        const roomAddrs: IRoomAddr = { id: 30150 };
        wamoliUser.roomAddrs = [roomAddrs];

        activatedRoute.data = of({ wamoliUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(wamoliUser));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.roomAddrsSharedCollection).toContain(roomAddrs);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliUser = { id: 123 };
        spyOn(wamoliUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wamoliUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(wamoliUserService.update).toHaveBeenCalledWith(wamoliUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliUser = new WamoliUser();
        spyOn(wamoliUserService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wamoliUser }));
        saveSubject.complete();

        // THEN
        expect(wamoliUserService.create).toHaveBeenCalledWith(wamoliUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliUser = { id: 123 };
        spyOn(wamoliUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(wamoliUserService.update).toHaveBeenCalledWith(wamoliUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackRoomAddrById', () => {
        it('Should return tracked RoomAddr primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRoomAddrById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedRoomAddr', () => {
        it('Should return option if no RoomAddr is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedRoomAddr(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected RoomAddr for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedRoomAddr(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this RoomAddr is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedRoomAddr(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
