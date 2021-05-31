import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WamoliUserLocationDetailComponent } from './wamoli-user-location-detail.component';

describe('Component Tests', () => {
  describe('WamoliUserLocation Management Detail Component', () => {
    let comp: WamoliUserLocationDetailComponent;
    let fixture: ComponentFixture<WamoliUserLocationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WamoliUserLocationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ wamoliUserLocation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WamoliUserLocationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WamoliUserLocationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load wamoliUserLocation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.wamoliUserLocation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
