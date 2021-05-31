import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITuYaDevice, TuYaDevice } from '../tu-ya-device.model';
import { TuYaDeviceService } from '../service/tu-ya-device.service';

@Injectable({ providedIn: 'root' })
export class TuYaDeviceRoutingResolveService implements Resolve<ITuYaDevice> {
  constructor(protected service: TuYaDeviceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITuYaDevice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tuYaDevice: HttpResponse<TuYaDevice>) => {
          if (tuYaDevice.body) {
            return of(tuYaDevice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TuYaDevice());
  }
}
