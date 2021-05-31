jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommunityImagesService } from '../service/community-images.service';
import { ICommunityImages, CommunityImages } from '../community-images.model';
import { ICommunityImageGroup } from 'app/entities/community-image-group/community-image-group.model';
import { CommunityImageGroupService } from 'app/entities/community-image-group/service/community-image-group.service';

import { CommunityImagesUpdateComponent } from './community-images-update.component';

describe('Component Tests', () => {
  describe('CommunityImages Management Update Component', () => {
    let comp: CommunityImagesUpdateComponent;
    let fixture: ComponentFixture<CommunityImagesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let communityImagesService: CommunityImagesService;
    let communityImageGroupService: CommunityImageGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommunityImagesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommunityImagesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommunityImagesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      communityImagesService = TestBed.inject(CommunityImagesService);
      communityImageGroupService = TestBed.inject(CommunityImageGroupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CommunityImageGroup query and add missing value', () => {
        const communityImages: ICommunityImages = { id: 456 };
        const communityImageGroup: ICommunityImageGroup = { id: 76658 };
        communityImages.communityImageGroup = communityImageGroup;

        const communityImageGroupCollection: ICommunityImageGroup[] = [{ id: 56344 }];
        spyOn(communityImageGroupService, 'query').and.returnValue(of(new HttpResponse({ body: communityImageGroupCollection })));
        const additionalCommunityImageGroups = [communityImageGroup];
        const expectedCollection: ICommunityImageGroup[] = [...additionalCommunityImageGroups, ...communityImageGroupCollection];
        spyOn(communityImageGroupService, 'addCommunityImageGroupToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ communityImages });
        comp.ngOnInit();

        expect(communityImageGroupService.query).toHaveBeenCalled();
        expect(communityImageGroupService.addCommunityImageGroupToCollectionIfMissing).toHaveBeenCalledWith(
          communityImageGroupCollection,
          ...additionalCommunityImageGroups
        );
        expect(comp.communityImageGroupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const communityImages: ICommunityImages = { id: 456 };
        const communityImageGroup: ICommunityImageGroup = { id: 82564 };
        communityImages.communityImageGroup = communityImageGroup;

        activatedRoute.data = of({ communityImages });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(communityImages));
        expect(comp.communityImageGroupsSharedCollection).toContain(communityImageGroup);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityImages = { id: 123 };
        spyOn(communityImagesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityImages });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communityImages }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(communityImagesService.update).toHaveBeenCalledWith(communityImages);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityImages = new CommunityImages();
        spyOn(communityImagesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityImages });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communityImages }));
        saveSubject.complete();

        // THEN
        expect(communityImagesService.create).toHaveBeenCalledWith(communityImages);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityImages = { id: 123 };
        spyOn(communityImagesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityImages });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(communityImagesService.update).toHaveBeenCalledWith(communityImages);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommunityImageGroupById', () => {
        it('Should return tracked CommunityImageGroup primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommunityImageGroupById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
