jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WamoliUserLocationService } from '../service/wamoli-user-location.service';
import { IWamoliUserLocation, WamoliUserLocation } from '../wamoli-user-location.model';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';
import { WamoliUserService } from 'app/entities/wamoli-user/service/wamoli-user.service';

import { WamoliUserLocationUpdateComponent } from './wamoli-user-location-update.component';

describe('Component Tests', () => {
  describe('WamoliUserLocation Management Update Component', () => {
    let comp: WamoliUserLocationUpdateComponent;
    let fixture: ComponentFixture<WamoliUserLocationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let wamoliUserLocationService: WamoliUserLocationService;
    let wamoliUserService: WamoliUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WamoliUserLocationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WamoliUserLocationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WamoliUserLocationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      wamoliUserLocationService = TestBed.inject(WamoliUserLocationService);
      wamoliUserService = TestBed.inject(WamoliUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call wamoliUser query and add missing value', () => {
        const wamoliUserLocation: IWamoliUserLocation = { id: 456 };
        const wamoliUser: IWamoliUser = { id: 83957 };
        wamoliUserLocation.wamoliUser = wamoliUser;

        const wamoliUserCollection: IWamoliUser[] = [{ id: 19917 }];
        spyOn(wamoliUserService, 'query').and.returnValue(of(new HttpResponse({ body: wamoliUserCollection })));
        const expectedCollection: IWamoliUser[] = [wamoliUser, ...wamoliUserCollection];
        spyOn(wamoliUserService, 'addWamoliUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ wamoliUserLocation });
        comp.ngOnInit();

        expect(wamoliUserService.query).toHaveBeenCalled();
        expect(wamoliUserService.addWamoliUserToCollectionIfMissing).toHaveBeenCalledWith(wamoliUserCollection, wamoliUser);
        expect(comp.wamoliUsersCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const wamoliUserLocation: IWamoliUserLocation = { id: 456 };
        const wamoliUser: IWamoliUser = { id: 58570 };
        wamoliUserLocation.wamoliUser = wamoliUser;

        activatedRoute.data = of({ wamoliUserLocation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(wamoliUserLocation));
        expect(comp.wamoliUsersCollection).toContain(wamoliUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliUserLocation = { id: 123 };
        spyOn(wamoliUserLocationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliUserLocation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wamoliUserLocation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(wamoliUserLocationService.update).toHaveBeenCalledWith(wamoliUserLocation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliUserLocation = new WamoliUserLocation();
        spyOn(wamoliUserLocationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliUserLocation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wamoliUserLocation }));
        saveSubject.complete();

        // THEN
        expect(wamoliUserLocationService.create).toHaveBeenCalledWith(wamoliUserLocation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliUserLocation = { id: 123 };
        spyOn(wamoliUserLocationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliUserLocation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(wamoliUserLocationService.update).toHaveBeenCalledWith(wamoliUserLocation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackWamoliUserById', () => {
        it('Should return tracked WamoliUser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWamoliUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
