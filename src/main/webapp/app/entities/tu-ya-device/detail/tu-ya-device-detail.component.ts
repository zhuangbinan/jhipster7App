import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITuYaDevice } from '../tu-ya-device.model';

@Component({
  selector: 'jhi-tu-ya-device-detail',
  templateUrl: './tu-ya-device-detail.component.html',
})
export class TuYaDeviceDetailComponent implements OnInit {
  tuYaDevice: ITuYaDevice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tuYaDevice }) => {
      this.tuYaDevice = tuYaDevice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
