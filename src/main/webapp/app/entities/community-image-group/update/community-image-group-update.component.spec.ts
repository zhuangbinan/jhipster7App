jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommunityImageGroupService } from '../service/community-image-group.service';
import { ICommunityImageGroup, CommunityImageGroup } from '../community-image-group.model';

import { CommunityImageGroupUpdateComponent } from './community-image-group-update.component';

describe('Component Tests', () => {
  describe('CommunityImageGroup Management Update Component', () => {
    let comp: CommunityImageGroupUpdateComponent;
    let fixture: ComponentFixture<CommunityImageGroupUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let communityImageGroupService: CommunityImageGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommunityImageGroupUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommunityImageGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommunityImageGroupUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      communityImageGroupService = TestBed.inject(CommunityImageGroupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const communityImageGroup: ICommunityImageGroup = { id: 456 };

        activatedRoute.data = of({ communityImageGroup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(communityImageGroup));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityImageGroup = { id: 123 };
        spyOn(communityImageGroupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityImageGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communityImageGroup }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(communityImageGroupService.update).toHaveBeenCalledWith(communityImageGroup);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityImageGroup = new CommunityImageGroup();
        spyOn(communityImageGroupService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityImageGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communityImageGroup }));
        saveSubject.complete();

        // THEN
        expect(communityImageGroupService.create).toHaveBeenCalledWith(communityImageGroup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityImageGroup = { id: 123 };
        spyOn(communityImageGroupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityImageGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(communityImageGroupService.update).toHaveBeenCalledWith(communityImageGroup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
