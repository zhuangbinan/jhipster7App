jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WamoliFaceLibraryService } from '../service/wamoli-face-library.service';
import { IWamoliFaceLibrary, WamoliFaceLibrary } from '../wamoli-face-library.model';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';
import { WamoliUserService } from 'app/entities/wamoli-user/service/wamoli-user.service';

import { WamoliFaceLibraryUpdateComponent } from './wamoli-face-library-update.component';

describe('Component Tests', () => {
  describe('WamoliFaceLibrary Management Update Component', () => {
    let comp: WamoliFaceLibraryUpdateComponent;
    let fixture: ComponentFixture<WamoliFaceLibraryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let wamoliFaceLibraryService: WamoliFaceLibraryService;
    let wamoliUserService: WamoliUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WamoliFaceLibraryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WamoliFaceLibraryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WamoliFaceLibraryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      wamoliFaceLibraryService = TestBed.inject(WamoliFaceLibraryService);
      wamoliUserService = TestBed.inject(WamoliUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call wamoliUser query and add missing value', () => {
        const wamoliFaceLibrary: IWamoliFaceLibrary = { id: 456 };
        const wamoliUser: IWamoliUser = { id: 71566 };
        wamoliFaceLibrary.wamoliUser = wamoliUser;

        const wamoliUserCollection: IWamoliUser[] = [{ id: 47144 }];
        spyOn(wamoliUserService, 'query').and.returnValue(of(new HttpResponse({ body: wamoliUserCollection })));
        const expectedCollection: IWamoliUser[] = [wamoliUser, ...wamoliUserCollection];
        spyOn(wamoliUserService, 'addWamoliUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ wamoliFaceLibrary });
        comp.ngOnInit();

        expect(wamoliUserService.query).toHaveBeenCalled();
        expect(wamoliUserService.addWamoliUserToCollectionIfMissing).toHaveBeenCalledWith(wamoliUserCollection, wamoliUser);
        expect(comp.wamoliUsersCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const wamoliFaceLibrary: IWamoliFaceLibrary = { id: 456 };
        const wamoliUser: IWamoliUser = { id: 92605 };
        wamoliFaceLibrary.wamoliUser = wamoliUser;

        activatedRoute.data = of({ wamoliFaceLibrary });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(wamoliFaceLibrary));
        expect(comp.wamoliUsersCollection).toContain(wamoliUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliFaceLibrary = { id: 123 };
        spyOn(wamoliFaceLibraryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliFaceLibrary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wamoliFaceLibrary }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(wamoliFaceLibraryService.update).toHaveBeenCalledWith(wamoliFaceLibrary);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliFaceLibrary = new WamoliFaceLibrary();
        spyOn(wamoliFaceLibraryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliFaceLibrary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wamoliFaceLibrary }));
        saveSubject.complete();

        // THEN
        expect(wamoliFaceLibraryService.create).toHaveBeenCalledWith(wamoliFaceLibrary);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wamoliFaceLibrary = { id: 123 };
        spyOn(wamoliFaceLibraryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wamoliFaceLibrary });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(wamoliFaceLibraryService.update).toHaveBeenCalledWith(wamoliFaceLibrary);
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
