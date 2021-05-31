import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TuYaDeviceDetailComponent } from './tu-ya-device-detail.component';

describe('Component Tests', () => {
  describe('TuYaDevice Management Detail Component', () => {
    let comp: TuYaDeviceDetailComponent;
    let fixture: ComponentFixture<TuYaDeviceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TuYaDeviceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tuYaDevice: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TuYaDeviceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TuYaDeviceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tuYaDevice on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tuYaDevice).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
