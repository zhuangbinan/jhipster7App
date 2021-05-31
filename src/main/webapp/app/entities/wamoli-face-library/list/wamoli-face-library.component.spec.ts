import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { WamoliFaceLibraryService } from '../service/wamoli-face-library.service';

import { WamoliFaceLibraryComponent } from './wamoli-face-library.component';

describe('Component Tests', () => {
  describe('WamoliFaceLibrary Management Component', () => {
    let comp: WamoliFaceLibraryComponent;
    let fixture: ComponentFixture<WamoliFaceLibraryComponent>;
    let service: WamoliFaceLibraryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WamoliFaceLibraryComponent],
      })
        .overrideTemplate(WamoliFaceLibraryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WamoliFaceLibraryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(WamoliFaceLibraryService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.wamoliFaceLibraries?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
