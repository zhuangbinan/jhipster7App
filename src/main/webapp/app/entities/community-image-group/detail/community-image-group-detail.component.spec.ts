import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommunityImageGroupDetailComponent } from './community-image-group-detail.component';

describe('Component Tests', () => {
  describe('CommunityImageGroup Management Detail Component', () => {
    let comp: CommunityImageGroupDetailComponent;
    let fixture: ComponentFixture<CommunityImageGroupDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CommunityImageGroupDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ communityImageGroup: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CommunityImageGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommunityImageGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load communityImageGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.communityImageGroup).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
