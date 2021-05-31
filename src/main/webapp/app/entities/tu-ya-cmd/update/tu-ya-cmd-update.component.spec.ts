jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TuYaCmdService } from '../service/tu-ya-cmd.service';
import { ITuYaCmd, TuYaCmd } from '../tu-ya-cmd.model';
import { ITuYaDevice } from 'app/entities/tu-ya-device/tu-ya-device.model';
import { TuYaDeviceService } from 'app/entities/tu-ya-device/service/tu-ya-device.service';

import { TuYaCmdUpdateComponent } from './tu-ya-cmd-update.component';

describe('Component Tests', () => {
  describe('TuYaCmd Management Update Component', () => {
    let comp: TuYaCmdUpdateComponent;
    let fixture: ComponentFixture<TuYaCmdUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tuYaCmdService: TuYaCmdService;
    let tuYaDeviceService: TuYaDeviceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TuYaCmdUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TuYaCmdUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TuYaCmdUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tuYaCmdService = TestBed.inject(TuYaCmdService);
      tuYaDeviceService = TestBed.inject(TuYaDeviceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TuYaDevice query and add missing value', () => {
        const tuYaCmd: ITuYaCmd = { id: 456 };
        const tuYaDevice: ITuYaDevice = { id: 78482 };
        tuYaCmd.tuYaDevice = tuYaDevice;

        const tuYaDeviceCollection: ITuYaDevice[] = [{ id: 14311 }];
        spyOn(tuYaDeviceService, 'query').and.returnValue(of(new HttpResponse({ body: tuYaDeviceCollection })));
        const additionalTuYaDevices = [tuYaDevice];
        const expectedCollection: ITuYaDevice[] = [...additionalTuYaDevices, ...tuYaDeviceCollection];
        spyOn(tuYaDeviceService, 'addTuYaDeviceToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ tuYaCmd });
        comp.ngOnInit();

        expect(tuYaDeviceService.query).toHaveBeenCalled();
        expect(tuYaDeviceService.addTuYaDeviceToCollectionIfMissing).toHaveBeenCalledWith(tuYaDeviceCollection, ...additionalTuYaDevices);
        expect(comp.tuYaDevicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tuYaCmd: ITuYaCmd = { id: 456 };
        const tuYaDevice: ITuYaDevice = { id: 1985 };
        tuYaCmd.tuYaDevice = tuYaDevice;

        activatedRoute.data = of({ tuYaCmd });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tuYaCmd));
        expect(comp.tuYaDevicesSharedCollection).toContain(tuYaDevice);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tuYaCmd = { id: 123 };
        spyOn(tuYaCmdService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tuYaCmd });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tuYaCmd }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tuYaCmdService.update).toHaveBeenCalledWith(tuYaCmd);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tuYaCmd = new TuYaCmd();
        spyOn(tuYaCmdService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tuYaCmd });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tuYaCmd }));
        saveSubject.complete();

        // THEN
        expect(tuYaCmdService.create).toHaveBeenCalledWith(tuYaCmd);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tuYaCmd = { id: 123 };
        spyOn(tuYaCmdService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tuYaCmd });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tuYaCmdService.update).toHaveBeenCalledWith(tuYaCmd);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTuYaDeviceById', () => {
        it('Should return tracked TuYaDevice primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTuYaDeviceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
