import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWamoliUser } from '../wamoli-user.model';

@Component({
  selector: 'jhi-wamoli-user-detail',
  templateUrl: './wamoli-user-detail.component.html',
})
export class WamoliUserDetailComponent implements OnInit {
  wamoliUser: IWamoliUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wamoliUser }) => {
      this.wamoliUser = wamoliUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
