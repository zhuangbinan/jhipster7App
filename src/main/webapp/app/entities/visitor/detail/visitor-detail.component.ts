import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVisitor } from '../visitor.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-visitor-detail',
  templateUrl: './visitor-detail.component.html',
})
export class VisitorDetailComponent implements OnInit {
  visitor: IVisitor | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visitor }) => {
      this.visitor = visitor;
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
