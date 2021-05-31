import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommunityImages } from '../community-images.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-community-images-detail',
  templateUrl: './community-images-detail.component.html',
})
export class CommunityImagesDetailComponent implements OnInit {
  communityImages: ICommunityImages | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ communityImages }) => {
      this.communityImages = communityImages;
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
