import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITuYaCmd, getTuYaCmdIdentifier } from '../tu-ya-cmd.model';

export type EntityResponseType = HttpResponse<ITuYaCmd>;
export type EntityArrayResponseType = HttpResponse<ITuYaCmd[]>;

@Injectable({ providedIn: 'root' })
export class TuYaCmdService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tu-ya-cmds');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(tuYaCmd: ITuYaCmd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tuYaCmd);
    return this.http
      .post<ITuYaCmd>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tuYaCmd: ITuYaCmd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tuYaCmd);
    return this.http
      .put<ITuYaCmd>(`${this.resourceUrl}/${getTuYaCmdIdentifier(tuYaCmd) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tuYaCmd: ITuYaCmd): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tuYaCmd);
    return this.http
      .patch<ITuYaCmd>(`${this.resourceUrl}/${getTuYaCmdIdentifier(tuYaCmd) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITuYaCmd>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITuYaCmd[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTuYaCmdToCollectionIfMissing(tuYaCmdCollection: ITuYaCmd[], ...tuYaCmdsToCheck: (ITuYaCmd | null | undefined)[]): ITuYaCmd[] {
    const tuYaCmds: ITuYaCmd[] = tuYaCmdsToCheck.filter(isPresent);
    if (tuYaCmds.length > 0) {
      const tuYaCmdCollectionIdentifiers = tuYaCmdCollection.map(tuYaCmdItem => getTuYaCmdIdentifier(tuYaCmdItem)!);
      const tuYaCmdsToAdd = tuYaCmds.filter(tuYaCmdItem => {
        const tuYaCmdIdentifier = getTuYaCmdIdentifier(tuYaCmdItem);
        if (tuYaCmdIdentifier == null || tuYaCmdCollectionIdentifiers.includes(tuYaCmdIdentifier)) {
          return false;
        }
        tuYaCmdCollectionIdentifiers.push(tuYaCmdIdentifier);
        return true;
      });
      return [...tuYaCmdsToAdd, ...tuYaCmdCollection];
    }
    return tuYaCmdCollection;
  }

  protected convertDateFromClient(tuYaCmd: ITuYaCmd): ITuYaCmd {
    return Object.assign({}, tuYaCmd, {
      createTime: tuYaCmd.createTime?.isValid() ? tuYaCmd.createTime.toJSON() : undefined,
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
      res.body.forEach((tuYaCmd: ITuYaCmd) => {
        tuYaCmd.createTime = tuYaCmd.createTime ? dayjs(tuYaCmd.createTime) : undefined;
      });
    }
    return res;
  }
}
