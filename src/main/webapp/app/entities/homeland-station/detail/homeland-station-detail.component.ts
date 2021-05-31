import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHomelandStation } from '../homeland-station.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-homeland-station-detail',
  templateUrl: './homeland-station-detail.component.html',
})
export class HomelandStationDetailComponent implements OnInit {
  homelandStation: IHomelandStation | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ homelandStation }) => {
      this.homelandStation = homelandStation;
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
