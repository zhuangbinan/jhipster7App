import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommunity } from '../community.model';

@Component({
  selector: 'jhi-community-detail',
  templateUrl: './community-detail.component.html',
})
export class CommunityDetailComponent implements OnInit {
  community: ICommunity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ community }) => {
      this.community = community;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
