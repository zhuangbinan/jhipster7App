jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VisitorService } from '../service/visitor.service';
import { IVisitor, Visitor } from '../visitor.model';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';
import { RoomAddrService } from 'app/entities/room-addr/service/room-addr.service';

import { VisitorUpdateComponent } from './visitor-update.component';

describe('Component Tests', () => {
  describe('Visitor Management Update Component', () => {
    let comp: VisitorUpdateComponent;
    let fixture: ComponentFixture<VisitorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let visitorService: VisitorService;
    let roomAddrService: RoomAddrService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VisitorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VisitorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VisitorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      visitorService = TestBed.inject(VisitorService);
      roomAddrService = TestBed.inject(RoomAddrService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call RoomAddr query and add missing value', () => {
        const visitor: IVisitor = { id: 456 };
        const roomAddr: IRoomAddr = { id: 45893 };
        visitor.roomAddr = roomAddr;

        const roomAddrCollection: IRoomAddr[] = [{ id: 48975 }];
        spyOn(roomAddrService, 'query').and.returnValue(of(new HttpResponse({ body: roomAddrCollection })));
        const additionalRoomAddrs = [roomAddr];
        const expectedCollection: IRoomAddr[] = [...additionalRoomAddrs, ...roomAddrCollection];
        spyOn(roomAddrService, 'addRoomAddrToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ visitor });
        comp.ngOnInit();

        expect(roomAddrService.query).toHaveBeenCalled();
        expect(roomAddrService.addRoomAddrToCollectionIfMissing).toHaveBeenCalledWith(roomAddrCollection, ...additionalRoomAddrs);
        expect(comp.roomAddrsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const visitor: IVisitor = { id: 456 };
        const roomAddr: IRoomAddr = { id: 71315 };
        visitor.roomAddr = roomAddr;

        activatedRoute.data = of({ visitor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(visitor));
        expect(comp.roomAddrsSharedCollection).toContain(roomAddr);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visitor = { id: 123 };
        spyOn(visitorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visitor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: visitor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(visitorService.update).toHaveBeenCalledWith(visitor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visitor = new Visitor();
        spyOn(visitorService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visitor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: visitor }));
        saveSubject.complete();

        // THEN
        expect(visitorService.create).toHaveBeenCalledWith(visitor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visitor = { id: 123 };
        spyOn(visitorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visitor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(visitorService.update).toHaveBeenCalledWith(visitor);
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
