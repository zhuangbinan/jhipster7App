import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomAddrDetailComponent } from './room-addr-detail.component';

describe('Component Tests', () => {
  describe('RoomAddr Management Detail Component', () => {
    let comp: RoomAddrDetailComponent;
    let fixture: ComponentFixture<RoomAddrDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RoomAddrDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ roomAddr: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RoomAddrDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RoomAddrDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load roomAddr on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.roomAddr).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
