jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommunityNoticeService } from '../service/community-notice.service';
import { ICommunityNotice, CommunityNotice } from '../community-notice.model';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { CommunityNoticeUpdateComponent } from './community-notice-update.component';

describe('Component Tests', () => {
  describe('CommunityNotice Management Update Component', () => {
    let comp: CommunityNoticeUpdateComponent;
    let fixture: ComponentFixture<CommunityNoticeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let communityNoticeService: CommunityNoticeService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommunityNoticeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommunityNoticeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommunityNoticeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      communityNoticeService = TestBed.inject(CommunityNoticeService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Community query and add missing value', () => {
        const communityNotice: ICommunityNotice = { id: 456 };
        const community: ICommunity = { id: 83768 };
        communityNotice.community = community;

        const communityCollection: ICommunity[] = [{ id: 17393 }];
        spyOn(communityService, 'query').and.returnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [community];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        spyOn(communityService, 'addCommunityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ communityNotice });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const communityNotice: ICommunityNotice = { id: 456 };
        const community: ICommunity = { id: 31241 };
        communityNotice.community = community;

        activatedRoute.data = of({ communityNotice });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(communityNotice));
        expect(comp.communitiesSharedCollection).toContain(community);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityNotice = { id: 123 };
        spyOn(communityNoticeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityNotice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communityNotice }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(communityNoticeService.update).toHaveBeenCalledWith(communityNotice);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityNotice = new CommunityNotice();
        spyOn(communityNoticeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityNotice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communityNotice }));
        saveSubject.complete();

        // THEN
        expect(communityNoticeService.create).toHaveBeenCalledWith(communityNotice);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityNotice = { id: 123 };
        spyOn(communityNoticeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityNotice });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(communityNoticeService.update).toHaveBeenCalledWith(communityNotice);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommunityById', () => {
        it('Should return tracked Community primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommunityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
