jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanyPostService } from '../service/company-post.service';
import { ICompanyPost, CompanyPost } from '../company-post.model';

import { CompanyPostUpdateComponent } from './company-post-update.component';

describe('Component Tests', () => {
  describe('CompanyPost Management Update Component', () => {
    let comp: CompanyPostUpdateComponent;
    let fixture: ComponentFixture<CompanyPostUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let companyPostService: CompanyPostService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const companyPost: ICompanyPost = { id: 456 };

        activatedRoute.data = of({ companyPost });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(companyPost));
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
  });
});
