import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { CommunityLeaderDetailComponent } from './community-leader-detail.component';

describe('Component Tests', () => {
  describe('CommunityLeader Management Detail Component', () => {
    let comp: CommunityLeaderDetailComponent;
    let fixture: ComponentFixture<CommunityLeaderDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CommunityLeaderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ communityLeader: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CommunityLeaderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommunityLeaderDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load communityLeader on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.communityLeader).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
