import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWamoliUserLocation } from '../wamoli-user-location.model';

@Component({
  selector: 'jhi-wamoli-user-location-detail',
  templateUrl: './wamoli-user-location-detail.component.html',
})
export class WamoliUserLocationDetailComponent implements OnInit {
  wamoliUserLocation: IWamoliUserLocation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wamoliUserLocation }) => {
      this.wamoliUserLocation = wamoliUserLocation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
