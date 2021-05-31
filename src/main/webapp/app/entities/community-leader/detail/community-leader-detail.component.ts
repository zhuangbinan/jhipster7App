import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommunityLeader } from '../community-leader.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-community-leader-detail',
  templateUrl: './community-leader-detail.component.html',
})
export class CommunityLeaderDetailComponent implements OnInit {
  communityLeader: ICommunityLeader | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communityLeader }) => {
      this.communityLeader = communityLeader;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
