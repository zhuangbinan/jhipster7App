import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITuYaDevice, getTuYaDeviceIdentifier } from '../tu-ya-device.model';

export type EntityResponseType = HttpResponse<ITuYaDevice>;
export type EntityArrayResponseType = HttpResponse<ITuYaDevice[]>;

@Injectable({ providedIn: 'root' })
export class TuYaDeviceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tu-ya-devices');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tuYaDevice: ITuYaDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tuYaDevice);
    return this.http
      .post<ITuYaDevice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tuYaDevice: ITuYaDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tuYaDevice);
    return this.http
      .put<ITuYaDevice>(`${this.resourceUrl}/${getTuYaDeviceIdentifier(tuYaDevice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tuYaDevice: ITuYaDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tuYaDevice);
    return this.http
      .patch<ITuYaDevice>(`${this.resourceUrl}/${getTuYaDeviceIdentifier(tuYaDevice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITuYaDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITuYaDevice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTuYaDeviceToCollectionIfMissing(
    tuYaDeviceCollection: ITuYaDevice[],
    ...tuYaDevicesToCheck: (ITuYaDevice | null | undefined)[]
  ): ITuYaDevice[] {
    const tuYaDevices: ITuYaDevice[] = tuYaDevicesToCheck.filter(isPresent);
    if (tuYaDevices.length > 0) {
      const tuYaDeviceCollectionIdentifiers = tuYaDeviceCollection.map(tuYaDeviceItem => getTuYaDeviceIdentifier(tuYaDeviceItem)!);
      const tuYaDevicesToAdd = tuYaDevices.filter(tuYaDeviceItem => {
        const tuYaDeviceIdentifier = getTuYaDeviceIdentifier(tuYaDeviceItem);
        if (tuYaDeviceIdentifier == null || tuYaDeviceCollectionIdentifiers.includes(tuYaDeviceIdentifier)) {
          return false;
        }
        tuYaDeviceCollectionIdentifiers.push(tuYaDeviceIdentifier);
        return true;
      });
      return [...tuYaDevicesToAdd, ...tuYaDeviceCollection];
    }
    return tuYaDeviceCollection;
  }

  protected convertDateFromClient(tuYaDevice: ITuYaDevice): ITuYaDevice {
    return Object.assign({}, tuYaDevice, {
      createTime: tuYaDevice.createTime?.isValid() ? tuYaDevice.createTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createTime = res.body.createTime ? dayjs(res.body.createTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tuYaDevice: ITuYaDevice) => {
        tuYaDevice.createTime = tuYaDevice.createTime ? dayjs(tuYaDevice.createTime) : undefined;
      });
    }
    return res;
  }
}
