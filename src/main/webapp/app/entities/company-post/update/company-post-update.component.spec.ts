jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanyPostService } from '../service/company-post.service';
import { ICompanyPost, CompanyPost } from '../company-post.model';
import { ICompanyDept } from 'app/entities/company-dept/company-dept.model';
import { CompanyDeptService } from 'app/entities/company-dept/service/company-dept.service';

import { CompanyPostUpdateComponent } from './company-post-update.component';

describe('Component Tests', () => {
  describe('CompanyPost Management Update Component', () => {
    let comp: CompanyPostUpdateComponent;
    let fixture: ComponentFixture<CompanyPostUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let companyPostService: CompanyPostService;
    let companyDeptService: CompanyDeptService;

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
      companyDeptService = TestBed.inject(CompanyDeptService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CompanyDept query and add missing value', () => {
        const companyPost: ICompanyPost = { id: 456 };
        const companyDept: ICompanyDept = { id: 31277 };
        companyPost.companyDept = companyDept;

        const companyDeptCollection: ICompanyDept[] = [{ id: 51357 }];
        spyOn(companyDeptService, 'query').and.returnValue(of(new HttpResponse({ body: companyDeptCollection })));
        const additionalCompanyDepts = [companyDept];
        const expectedCollection: ICompanyDept[] = [...additionalCompanyDepts, ...companyDeptCollection];
        spyOn(companyDeptService, 'addCompanyDeptToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ companyPost });
        comp.ngOnInit();

        expect(companyDeptService.query).toHaveBeenCalled();
        expect(companyDeptService.addCompanyDeptToCollectionIfMissing).toHaveBeenCalledWith(
          companyDeptCollection,
          ...additionalCompanyDepts
        );
        expect(comp.companyDeptsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const companyPost: ICompanyPost = { id: 456 };
        const companyDept: ICompanyDept = { id: 51184 };
        companyPost.companyDept = companyDept;

        activatedRoute.data = of({ companyPost });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(companyPost));
        expect(comp.companyDeptsSharedCollection).toContain(companyDept);
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
      describe('trackCompanyDeptById', () => {
        it('Should return tracked CompanyDept primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCompanyDeptById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
