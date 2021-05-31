jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HomelandStationService } from '../service/homeland-station.service';
import { IHomelandStation, HomelandStation } from '../homeland-station.model';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { HomelandStationUpdateComponent } from './homeland-station-update.component';

describe('Component Tests', () => {
  describe('HomelandStation Management Update Component', () => {
    let comp: HomelandStationUpdateComponent;
    let fixture: ComponentFixture<HomelandStationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let homelandStationService: HomelandStationService;
    let communityService: CommunityService;
    let companyService: CompanyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HomelandStationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(HomelandStationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HomelandStationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      homelandStationService = TestBed.inject(HomelandStationService);
      communityService = TestBed.inject(CommunityService);
      companyService = TestBed.inject(CompanyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Community query and add missing value', () => {
        const homelandStation: IHomelandStation = { id: 456 };
        const community: ICommunity = { id: 60083 };
        homelandStation.community = community;

        const communityCollection: ICommunity[] = [{ id: 8011 }];
        spyOn(communityService, 'query').and.returnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [community];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        spyOn(communityService, 'addCommunityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ homelandStation });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Company query and add missing value', () => {
        const homelandStation: IHomelandStation = { id: 456 };
        const company: ICompany = { id: 51319 };
        homelandStation.company = company;

        const companyCollection: ICompany[] = [{ id: 61068 }];
        spyOn(companyService, 'query').and.returnValue(of(new HttpResponse({ body: companyCollection })));
        const additionalCompanies = [company];
        const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
        spyOn(companyService, 'addCompanyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ homelandStation });
        comp.ngOnInit();

        expect(companyService.query).toHaveBeenCalled();
        expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
        expect(comp.companiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const homelandStation: IHomelandStation = { id: 456 };
        const community: ICommunity = { id: 51816 };
        homelandStation.community = community;
        const company: ICompany = { id: 92216 };
        homelandStation.company = company;

        activatedRoute.data = of({ homelandStation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(homelandStation));
        expect(comp.communitiesSharedCollection).toContain(community);
        expect(comp.companiesSharedCollection).toContain(company);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const homelandStation = { id: 123 };
        spyOn(homelandStationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ homelandStation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: homelandStation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(homelandStationService.update).toHaveBeenCalledWith(homelandStation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const homelandStation = new HomelandStation();
        spyOn(homelandStationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ homelandStation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: homelandStation }));
        saveSubject.complete();

        // THEN
        expect(homelandStationService.create).toHaveBeenCalledWith(homelandStation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const homelandStation = { id: 123 };
        spyOn(homelandStationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ homelandStation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(homelandStationService.update).toHaveBeenCalledWith(homelandStation);
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

      describe('trackCompanyById', () => {
        it('Should return tracked Company primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCompanyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
