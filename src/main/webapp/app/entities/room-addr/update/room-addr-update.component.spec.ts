jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RoomAddrService } from '../service/room-addr.service';
import { IRoomAddr, RoomAddr } from '../room-addr.model';
import { IBuildings } from 'app/entities/buildings/buildings.model';
import { BuildingsService } from 'app/entities/buildings/service/buildings.service';

import { RoomAddrUpdateComponent } from './room-addr-update.component';

describe('Component Tests', () => {
  describe('RoomAddr Management Update Component', () => {
    let comp: RoomAddrUpdateComponent;
    let fixture: ComponentFixture<RoomAddrUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let roomAddrService: RoomAddrService;
    let buildingsService: BuildingsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RoomAddrUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RoomAddrUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RoomAddrUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      roomAddrService = TestBed.inject(RoomAddrService);
      buildingsService = TestBed.inject(BuildingsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Buildings query and add missing value', () => {
        const roomAddr: IRoomAddr = { id: 456 };
        const buildings: IBuildings = { id: 41371 };
        roomAddr.buildings = buildings;

        const buildingsCollection: IBuildings[] = [{ id: 4412 }];
        spyOn(buildingsService, 'query').and.returnValue(of(new HttpResponse({ body: buildingsCollection })));
        const additionalBuildings = [buildings];
        const expectedCollection: IBuildings[] = [...additionalBuildings, ...buildingsCollection];
        spyOn(buildingsService, 'addBuildingsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ roomAddr });
        comp.ngOnInit();

        expect(buildingsService.query).toHaveBeenCalled();
        expect(buildingsService.addBuildingsToCollectionIfMissing).toHaveBeenCalledWith(buildingsCollection, ...additionalBuildings);
        expect(comp.buildingsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const roomAddr: IRoomAddr = { id: 456 };
        const buildings: IBuildings = { id: 26829 };
        roomAddr.buildings = buildings;

        activatedRoute.data = of({ roomAddr });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(roomAddr));
        expect(comp.buildingsSharedCollection).toContain(buildings);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roomAddr = { id: 123 };
        spyOn(roomAddrService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roomAddr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: roomAddr }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(roomAddrService.update).toHaveBeenCalledWith(roomAddr);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roomAddr = new RoomAddr();
        spyOn(roomAddrService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roomAddr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: roomAddr }));
        saveSubject.complete();

        // THEN
        expect(roomAddrService.create).toHaveBeenCalledWith(roomAddr);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roomAddr = { id: 123 };
        spyOn(roomAddrService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roomAddr });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(roomAddrService.update).toHaveBeenCalledWith(roomAddr);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBuildingsById', () => {
        it('Should return tracked Buildings primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBuildingsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
