import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BuildingsDetailComponent } from './buildings-detail.component';

describe('Component Tests', () => {
  describe('Buildings Management Detail Component', () => {
    let comp: BuildingsDetailComponent;
    let fixture: ComponentFixture<BuildingsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BuildingsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ buildings: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BuildingsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BuildingsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load buildings on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.buildings).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
