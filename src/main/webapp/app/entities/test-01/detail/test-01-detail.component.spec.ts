import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { Test01DetailComponent } from './test-01-detail.component';

describe('Component Tests', () => {
  describe('Test01 Management Detail Component', () => {
    let comp: Test01DetailComponent;
    let fixture: ComponentFixture<Test01DetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [Test01DetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ test01: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(Test01DetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Test01DetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load test01 on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.test01).toEqual(jasmine.objectContaining({ id: 123 }));
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
