import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompanyPostDetailComponent } from './company-post-detail.component';

describe('Component Tests', () => {
  describe('CompanyPost Management Detail Component', () => {
    let comp: CompanyPostDetailComponent;
    let fixture: ComponentFixture<CompanyPostDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CompanyPostDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ companyPost: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CompanyPostDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CompanyPostDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load companyPost on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.companyPost).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
