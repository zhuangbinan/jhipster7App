jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanyPostService } from '../service/company-post.service';
import { ICompanyPost, CompanyPost } from '../company-post.model';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';
import { WamoliUserService } from 'app/entities/wamoli-user/service/wamoli-user.service';

import { CompanyPostUpdateComponent } from './company-post-update.component';

describe('Component Tests', () => {
  describe('CompanyPost Management Update Component', () => {
    let comp: CompanyPostUpdateComponent;
    let fixture: ComponentFixture<CompanyPostUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let companyPostService: CompanyPostService;
    let wamoliUserService: WamoliUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompanyPostUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompanyPostUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompanyPostUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      companyPostService = TestBed.inject(CompanyPostService);
      wamoliUserService = TestBed.inject(WamoliUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call WamoliUser query and add missing value', () => {
        const companyPost: ICompanyPost = { id: 456 };
        const wamoliUsers: IWamoliUser[] = [{ id: 56017 }];
        companyPost.wamoliUsers = wamoliUsers;

        const wamoliUserCollection: IWamoliUser[] = [{ id: 77967 }];
        spyOn(wamoliUserService, 'query').and.returnValue(of(new HttpResponse({ body: wamoliUserCollection })));
        const additionalWamoliUsers = [...wamoliUsers];
        const expectedCollection: IWamoliUser[] = [...additionalWamoliUsers, ...wamoliUserCollection];
        spyOn(wamoliUserService, 'addWamoliUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ companyPost });
        comp.ngOnInit();

        expect(wamoliUserService.query).toHaveBeenCalled();
        expect(wamoliUserService.addWamoliUserToCollectionIfMissing).toHaveBeenCalledWith(wamoliUserCollection, ...additionalWamoliUsers);
        expect(comp.wamoliUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const companyPost: ICompanyPost = { id: 456 };
        const wamoliUsers: IWamoliUser = { id: 72709 };
        companyPost.wamoliUsers = [wamoliUsers];

        activatedRoute.data = of({ companyPost });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(companyPost));
        expect(comp.wamoliUsersSharedCollection).toContain(wamoliUsers);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyPost = { id: 123 };
        spyOn(companyPostService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: companyPost }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(companyPostService.update).toHaveBeenCalledWith(companyPost);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyPost = new CompanyPost();
        spyOn(companyPostService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: companyPost }));
        saveSubject.complete();

        // THEN
        expect(companyPostService.create).toHaveBeenCalledWith(companyPost);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyPost = { id: 123 };
        spyOn(companyPostService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(companyPostService.update).toHaveBeenCalledWith(companyPost);
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

    describe('Getting selected relationships', () => {
      describe('getSelectedWamoliUser', () => {
        it('Should return option if no WamoliUser is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedWamoliUser(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected WamoliUser for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedWamoliUser(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this WamoliUser is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedWamoliUser(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
