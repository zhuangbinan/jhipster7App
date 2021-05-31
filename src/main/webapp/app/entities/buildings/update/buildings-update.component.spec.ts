jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BuildingsService } from '../service/buildings.service';
import { IBuildings, Buildings } from '../buildings.model';
import { IHomelandStation } from 'app/entities/homeland-station/homeland-station.model';
import { HomelandStationService } from 'app/entities/homeland-station/service/homeland-station.service';

import { BuildingsUpdateComponent } from './buildings-update.component';

describe('Component Tests', () => {
  describe('Buildings Management Update Component', () => {
    let comp: BuildingsUpdateComponent;
    let fixture: ComponentFixture<BuildingsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let buildingsService: BuildingsService;
    let homelandStationService: HomelandStationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BuildingsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BuildingsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BuildingsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      buildingsService = TestBed.inject(BuildingsService);
      homelandStationService = TestBed.inject(HomelandStationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call HomelandStation query and add missing value', () => {
        const buildings: IBuildings = { id: 456 };
        const homelandStation: IHomelandStation = { id: 76295 };
        buildings.homelandStation = homelandStation;

        const homelandStationCollection: IHomelandStation[] = [{ id: 70784 }];
        spyOn(homelandStationService, 'query').and.returnValue(of(new HttpResponse({ body: homelandStationCollection })));
        const additionalHomelandStations = [homelandStation];
        const expectedCollection: IHomelandStation[] = [...additionalHomelandStations, ...homelandStationCollection];
        spyOn(homelandStationService, 'addHomelandStationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ buildings });
        comp.ngOnInit();

        expect(homelandStationService.query).toHaveBeenCalled();
        expect(homelandStationService.addHomelandStationToCollectionIfMissing).toHaveBeenCalledWith(
          homelandStationCollection,
          ...additionalHomelandStations
        );
        expect(comp.homelandStationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const buildings: IBuildings = { id: 456 };
        const homelandStation: IHomelandStation = { id: 40358 };
        buildings.homelandStation = homelandStation;

        activatedRoute.data = of({ buildings });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(buildings));
        expect(comp.homelandStationsSharedCollection).toContain(homelandStation);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const buildings = { id: 123 };
        spyOn(buildingsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ buildings });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: buildings }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(buildingsService.update).toHaveBeenCalledWith(buildings);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const buildings = new Buildings();
        spyOn(buildingsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ buildings });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: buildings }));
        saveSubject.complete();

        // THEN
        expect(buildingsService.create).toHaveBeenCalledWith(buildings);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const buildings = { id: 123 };
        spyOn(buildingsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ buildings });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(buildingsService.update).toHaveBeenCalledWith(buildings);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackHomelandStationById', () => {
        it('Should return tracked HomelandStation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackHomelandStationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
