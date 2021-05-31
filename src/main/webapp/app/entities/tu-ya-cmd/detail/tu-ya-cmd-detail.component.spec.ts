import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TuYaCmdDetailComponent } from './tu-ya-cmd-detail.component';

describe('Component Tests', () => {
  describe('TuYaCmd Management Detail Component', () => {
    let comp: TuYaCmdDetailComponent;
    let fixture: ComponentFixture<TuYaCmdDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TuYaCmdDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tuYaCmd: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TuYaCmdDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TuYaCmdDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tuYaCmd on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tuYaCmd).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
