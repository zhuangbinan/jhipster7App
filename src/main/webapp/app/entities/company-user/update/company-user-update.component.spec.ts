jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanyUserService } from '../service/company-user.service';
import { ICompanyUser, CompanyUser } from '../company-user.model';
import { ICompanyDept } from 'app/entities/company-dept/company-dept.model';
import { CompanyDeptService } from 'app/entities/company-dept/service/company-dept.service';
import { ICompanyPost } from 'app/entities/company-post/company-post.model';
import { CompanyPostService } from 'app/entities/company-post/service/company-post.service';

import { CompanyUserUpdateComponent } from './company-user-update.component';

describe('Component Tests', () => {
  describe('CompanyUser Management Update Component', () => {
    let comp: CompanyUserUpdateComponent;
    let fixture: ComponentFixture<CompanyUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let companyUserService: CompanyUserService;
    let companyDeptService: CompanyDeptService;
    let companyPostService: CompanyPostService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompanyUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompanyUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompanyUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      companyUserService = TestBed.inject(CompanyUserService);
      companyDeptService = TestBed.inject(CompanyDeptService);
      companyPostService = TestBed.inject(CompanyPostService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CompanyDept query and add missing value', () => {
        const companyUser: ICompanyUser = { id: 456 };
        const companyDepts: ICompanyDept[] = [{ id: 31277 }];
        companyUser.companyDepts = companyDepts;

        const companyDeptCollection: ICompanyDept[] = [{ id: 51357 }];
        spyOn(companyDeptService, 'query').and.returnValue(of(new HttpResponse({ body: companyDeptCollection })));
        const additionalCompanyDepts = [...companyDepts];
        const expectedCollection: ICompanyDept[] = [...additionalCompanyDepts, ...companyDeptCollection];
        spyOn(companyDeptService, 'addCompanyDeptToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ companyUser });
        comp.ngOnInit();

        expect(companyDeptService.query).toHaveBeenCalled();
        expect(companyDeptService.addCompanyDeptToCollectionIfMissing).toHaveBeenCalledWith(
          companyDeptCollection,
          ...additionalCompanyDepts
        );
        expect(comp.companyDeptsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CompanyPost query and add missing value', () => {
        const companyUser: ICompanyUser = { id: 456 };
        const companyPosts: ICompanyPost[] = [{ id: 20706 }];
        companyUser.companyPosts = companyPosts;

        const companyPostCollection: ICompanyPost[] = [{ id: 24414 }];
        spyOn(companyPostService, 'query').and.returnValue(of(new HttpResponse({ body: companyPostCollection })));
        const additionalCompanyPosts = [...companyPosts];
        const expectedCollection: ICompanyPost[] = [...additionalCompanyPosts, ...companyPostCollection];
        spyOn(companyPostService, 'addCompanyPostToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ companyUser });
        comp.ngOnInit();

        expect(companyPostService.query).toHaveBeenCalled();
        expect(companyPostService.addCompanyPostToCollectionIfMissing).toHaveBeenCalledWith(
          companyPostCollection,
          ...additionalCompanyPosts
        );
        expect(comp.companyPostsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const companyUser: ICompanyUser = { id: 456 };
        const companyDepts: ICompanyDept = { id: 51184 };
        companyUser.companyDepts = [companyDepts];
        const companyPosts: ICompanyPost = { id: 87925 };
        companyUser.companyPosts = [companyPosts];

        activatedRoute.data = of({ companyUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(companyUser));
        expect(comp.companyDeptsSharedCollection).toContain(companyDepts);
        expect(comp.companyPostsSharedCollection).toContain(companyPosts);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyUser = { id: 123 };
        spyOn(companyUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: companyUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(companyUserService.update).toHaveBeenCalledWith(companyUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyUser = new CompanyUser();
        spyOn(companyUserService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: companyUser }));
        saveSubject.complete();

        // THEN
        expect(companyUserService.create).toHaveBeenCalledWith(companyUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyUser = { id: 123 };
        spyOn(companyUserService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(companyUserService.update).toHaveBeenCalledWith(companyUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCompanyDeptById', () => {
        it('Should return tracked CompanyDept primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCompanyDeptById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCompanyPostById', () => {
        it('Should return tracked CompanyPost primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCompanyPostById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedCompanyDept', () => {
        it('Should return option if no CompanyDept is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedCompanyDept(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected CompanyDept for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedCompanyDept(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this CompanyDept is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedCompanyDept(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedCompanyPost', () => {
        it('Should return option if no CompanyPost is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedCompanyPost(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected CompanyPost for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedCompanyPost(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this CompanyPost is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedCompanyPost(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
