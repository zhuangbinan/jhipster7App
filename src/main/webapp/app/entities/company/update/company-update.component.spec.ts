jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanyService } from '../service/company.service';
import { ICompany, Company } from '../company.model';

import { CompanyUpdateComponent } from './company-update.component';

describe('Component Tests', () => {
  describe('Company Management Update Component', () => {
    let comp: CompanyUpdateComponent;
    let fixture: ComponentFixture<CompanyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let companyService: CompanyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompanyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompanyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompanyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      companyService = TestBed.inject(CompanyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const company: ICompany = { id: 456 };

        activatedRoute.data = of({ company });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(company));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const company = { id: 123 };
        spyOn(companyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ company });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: company }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(companyService.update).toHaveBeenCalledWith(company);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const company = new Company();
        spyOn(companyService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ company });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: company }));
        saveSubject.complete();

        // THEN
        expect(companyService.create).toHaveBeenCalledWith(company);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const company = { id: 123 };
        spyOn(companyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ company });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(companyService.update).toHaveBeenCalledWith(company);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
