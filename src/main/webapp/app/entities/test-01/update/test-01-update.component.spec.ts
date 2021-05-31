jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { Test01Service } from '../service/test-01.service';
import { ITest01, Test01 } from '../test-01.model';

import { Test01UpdateComponent } from './test-01-update.component';

describe('Component Tests', () => {
  describe('Test01 Management Update Component', () => {
    let comp: Test01UpdateComponent;
    let fixture: ComponentFixture<Test01UpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let test01Service: Test01Service;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [Test01UpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(Test01UpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Test01UpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      test01Service = TestBed.inject(Test01Service);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const test01: ITest01 = { id: 456 };

        activatedRoute.data = of({ test01 });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(test01));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const test01 = { id: 123 };
        spyOn(test01Service, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ test01 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: test01 }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(test01Service.update).toHaveBeenCalledWith(test01);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const test01 = new Test01();
        spyOn(test01Service, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ test01 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: test01 }));
        saveSubject.complete();

        // THEN
        expect(test01Service.create).toHaveBeenCalledWith(test01);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const test01 = { id: 123 };
        spyOn(test01Service, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ test01 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(test01Service.update).toHaveBeenCalledWith(test01);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
