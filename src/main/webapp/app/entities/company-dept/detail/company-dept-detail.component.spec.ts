import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompanyDeptDetailComponent } from './company-dept-detail.component';

describe('Component Tests', () => {
  describe('CompanyDept Management Detail Component', () => {
    let comp: CompanyDeptDetailComponent;
    let fixture: ComponentFixture<CompanyDeptDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CompanyDeptDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ companyDept: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CompanyDeptDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CompanyDeptDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load companyDept on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.companyDept).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
