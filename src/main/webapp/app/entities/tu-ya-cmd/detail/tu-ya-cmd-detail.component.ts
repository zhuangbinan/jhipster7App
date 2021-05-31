import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITuYaCmd } from '../tu-ya-cmd.model';

@Component({
  selector: 'jhi-tu-ya-cmd-detail',
  templateUrl: './tu-ya-cmd-detail.component.html',
})
export class TuYaCmdDetailComponent implements OnInit {
  tuYaCmd: ITuYaCmd | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tuYaCmd }) => {
      this.tuYaCmd = tuYaCmd;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
