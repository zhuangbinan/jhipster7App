jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TuYaDeviceService } from '../service/tu-ya-device.service';
import { ITuYaDevice, TuYaDevice } from '../tu-ya-device.model';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';
import { RoomAddrService } from 'app/entities/room-addr/service/room-addr.service';

import { TuYaDeviceUpdateComponent } from './tu-ya-device-update.component';

describe('Component Tests', () => {
  describe('TuYaDevice Management Update Component', () => {
    let comp: TuYaDeviceUpdateComponent;
    let fixture: ComponentFixture<TuYaDeviceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tuYaDeviceService: TuYaDeviceService;
    let roomAddrService: RoomAddrService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TuYaDeviceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TuYaDeviceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TuYaDeviceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tuYaDeviceService = TestBed.inject(TuYaDeviceService);
      roomAddrService = TestBed.inject(RoomAddrService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call RoomAddr query and add missing value', () => {
        const tuYaDevice: ITuYaDevice = { id: 456 };
        const roomAddr: IRoomAddr = { id: 46169 };
        tuYaDevice.roomAddr = roomAddr;

        const roomAddrCollection: IRoomAddr[] = [{ id: 73614 }];
        spyOn(roomAddrService, 'query').and.returnValue(of(new HttpResponse({ body: roomAddrCollection })));
        const additionalRoomAddrs = [roomAddr];
        const expectedCollection: IRoomAddr[] = [...additionalRoomAddrs, ...roomAddrCollection];
        spyOn(roomAddrService, 'addRoomAddrToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ tuYaDevice });
        comp.ngOnInit();

        expect(roomAddrService.query).toHaveBeenCalled();
        expect(roomAddrService.addRoomAddrToCollectionIfMissing).toHaveBeenCalledWith(roomAddrCollection, ...additionalRoomAddrs);
        expect(comp.roomAddrsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tuYaDevice: ITuYaDevice = { id: 456 };
        const roomAddr: IRoomAddr = { id: 25817 };
        tuYaDevice.roomAddr = roomAddr;

        activatedRoute.data = of({ tuYaDevice });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tuYaDevice));
        expect(comp.roomAddrsSharedCollection).toContain(roomAddr);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tuYaDevice = { id: 123 };
        spyOn(tuYaDeviceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tuYaDevice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tuYaDevice }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tuYaDeviceService.update).toHaveBeenCalledWith(tuYaDevice);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tuYaDevice = new TuYaDevice();
        spyOn(tuYaDeviceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tuYaDevice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tuYaDevice }));
        saveSubject.complete();

        // THEN
        expect(tuYaDeviceService.create).toHaveBeenCalledWith(tuYaDevice);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tuYaDevice = { id: 123 };
        spyOn(tuYaDeviceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tuYaDevice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tuYaDeviceService.update).toHaveBeenCalledWith(tuYaDevice);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRoomAddrById', () => {
        it('Should return tracked RoomAddr primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRoomAddrById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
