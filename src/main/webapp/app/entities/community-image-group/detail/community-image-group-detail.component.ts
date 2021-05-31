import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommunityImageGroup } from '../community-image-group.model';

@Component({
  selector: 'jhi-community-image-group-detail',
  templateUrl: './community-image-group-detail.component.html',
})
export class CommunityImageGroupDetailComponent implements OnInit {
  communityImageGroup: ICommunityImageGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communityImageGroup }) => {
      this.communityImageGroup = communityImageGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
