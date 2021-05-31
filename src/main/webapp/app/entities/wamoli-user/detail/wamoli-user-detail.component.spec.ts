import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WamoliUserDetailComponent } from './wamoli-user-detail.component';

describe('Component Tests', () => {
  describe('WamoliUser Management Detail Component', () => {
    let comp: WamoliUserDetailComponent;
    let fixture: ComponentFixture<WamoliUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WamoliUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ wamoliUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WamoliUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WamoliUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load wamoliUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.wamoliUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
