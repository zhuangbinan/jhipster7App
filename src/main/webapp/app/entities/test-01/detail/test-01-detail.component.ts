import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITest01 } from '../test-01.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-test-01-detail',
  templateUrl: './test-01-detail.component.html',
})
export class Test01DetailComponent implements OnInit {
  test01: ITest01 | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ test01 }) => {
      this.test01 = test01;
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
