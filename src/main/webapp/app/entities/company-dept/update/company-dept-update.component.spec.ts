jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanyDeptService } from '../service/company-dept.service';
import { ICompanyDept, CompanyDept } from '../company-dept.model';

import { CompanyDeptUpdateComponent } from './company-dept-update.component';

describe('Component Tests', () => {
  describe('CompanyDept Management Update Component', () => {
    let comp: CompanyDeptUpdateComponent;
    let fixture: ComponentFixture<CompanyDeptUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let companyDeptService: CompanyDeptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompanyDeptUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompanyDeptUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompanyDeptUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      companyDeptService = TestBed.inject(CompanyDeptService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const companyDept: ICompanyDept = { id: 456 };

        activatedRoute.data = of({ companyDept });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(companyDept));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyDept = { id: 123 };
        spyOn(companyDeptService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyDept });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: companyDept }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(companyDeptService.update).toHaveBeenCalledWith(companyDept);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyDept = new CompanyDept();
        spyOn(companyDeptService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyDept });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: companyDept }));
        saveSubject.complete();

        // THEN
        expect(companyDeptService.create).toHaveBeenCalledWith(companyDept);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companyDept = { id: 123 };
        spyOn(companyDeptService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companyDept });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(companyDeptService.update).toHaveBeenCalledWith(companyDept);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
