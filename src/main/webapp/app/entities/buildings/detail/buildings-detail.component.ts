import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBuildings } from '../buildings.model';

@Component({
  selector: 'jhi-buildings-detail',
  templateUrl: './buildings-detail.component.html',
})
export class BuildingsDetailComponent implements OnInit {
  buildings: IBuildings | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ buildings }) => {
      this.buildings = buildings;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
