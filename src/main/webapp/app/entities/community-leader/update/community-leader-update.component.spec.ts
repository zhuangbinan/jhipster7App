jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommunityLeaderService } from '../service/community-leader.service';
import { ICommunityLeader, CommunityLeader } from '../community-leader.model';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { CommunityLeaderUpdateComponent } from './community-leader-update.component';

describe('Component Tests', () => {
  describe('CommunityLeader Management Update Component', () => {
    let comp: CommunityLeaderUpdateComponent;
    let fixture: ComponentFixture<CommunityLeaderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let communityLeaderService: CommunityLeaderService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommunityLeaderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommunityLeaderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommunityLeaderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      communityLeaderService = TestBed.inject(CommunityLeaderService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Community query and add missing value', () => {
        const communityLeader: ICommunityLeader = { id: 456 };
        const community: ICommunity = { id: 75322 };
        communityLeader.community = community;

        const communityCollection: ICommunity[] = [{ id: 92047 }];
        spyOn(communityService, 'query').and.returnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [community];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        spyOn(communityService, 'addCommunityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ communityLeader });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const communityLeader: ICommunityLeader = { id: 456 };
        const community: ICommunity = { id: 57700 };
        communityLeader.community = community;

        activatedRoute.data = of({ communityLeader });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(communityLeader));
        expect(comp.communitiesSharedCollection).toContain(community);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityLeader = { id: 123 };
        spyOn(communityLeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityLeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communityLeader }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(communityLeaderService.update).toHaveBeenCalledWith(communityLeader);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityLeader = new CommunityLeader();
        spyOn(communityLeaderService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityLeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: communityLeader }));
        saveSubject.complete();

        // THEN
        expect(communityLeaderService.create).toHaveBeenCalledWith(communityLeader);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const communityLeader = { id: 123 };
        spyOn(communityLeaderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ communityLeader });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(communityLeaderService.update).toHaveBeenCalledWith(communityLeader);
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
