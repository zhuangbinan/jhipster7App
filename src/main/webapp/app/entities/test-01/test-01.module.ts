import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { Test01Component } from './list/test-01.component';
import { Test01DetailComponent } from './detail/test-01-detail.component';
import { Test01UpdateComponent } from './update/test-01-update.component';
import { Test01DeleteDialogComponent } from './delete/test-01-delete-dialog.component';
import { Test01RoutingModule } from './route/test-01-routing.module';

@NgModule({
  imports: [SharedModule, Test01RoutingModule],
  declarations: [Test01Component, Test01DetailComponent, Test01UpdateComponent, Test01DeleteDialogComponent],
  entryComponents: [Test01DeleteDialogComponent],
})
export class Test01Module {}
