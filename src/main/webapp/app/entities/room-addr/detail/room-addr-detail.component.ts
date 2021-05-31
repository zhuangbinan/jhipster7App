import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomAddr } from '../room-addr.model';

@Component({
  selector: 'jhi-room-addr-detail',
  templateUrl: './room-addr-detail.component.html',
})
export class RoomAddrDetailComponent implements OnInit {
  roomAddr: IRoomAddr | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomAddr }) => {
      this.roomAddr = roomAddr;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
