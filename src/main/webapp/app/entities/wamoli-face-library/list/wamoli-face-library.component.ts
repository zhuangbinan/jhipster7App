import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWamoliFaceLibrary } from '../wamoli-face-library.model';
import { WamoliFaceLibraryService } from '../service/wamoli-face-library.service';
import { WamoliFaceLibraryDeleteDialogComponent } from '../delete/wamoli-face-library-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-wamoli-face-library',
  templateUrl: './wamoli-face-library.component.html',
})
export class WamoliFaceLibraryComponent implements OnInit {
  wamoliFaceLibraries?: IWamoliFaceLibrary[];
  isLoading = false;

  constructor(
    protected wamoliFaceLibraryService: WamoliFaceLibraryService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.wamoliFaceLibraryService.query().subscribe(
      (res: HttpResponse<IWamoliFaceLibrary[]>) => {
        this.isLoading = false;
        this.wamoliFaceLibraries = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWamoliFaceLibrary): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(wamoliFaceLibrary: IWamoliFaceLibrary): void {
    const modalRef = this.modalService.open(WamoliFaceLibraryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.wamoliFaceLibrary = wamoliFaceLibrary;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
