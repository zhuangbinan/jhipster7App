import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoomAddr, getRoomAddrIdentifier } from '../room-addr.model';

export type EntityResponseType = HttpResponse<IRoomAddr>;
export type EntityArrayResponseType = HttpResponse<IRoomAddr[]>;

@Injectable({ providedIn: 'root' })
export class RoomAddrService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/room-addrs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(roomAddr: IRoomAddr): Observable<EntityResponseType> {
    return this.http.post<IRoomAddr>(this.resourceUrl, roomAddr, { observe: 'response' });
  }

  update(roomAddr: IRoomAddr): Observable<EntityResponseType> {
    return this.http.put<IRoomAddr>(`${this.resourceUrl}/${getRoomAddrIdentifier(roomAddr) as number}`, roomAddr, { observe: 'response' });
  }

  partialUpdate(roomAddr: IRoomAddr): Observable<EntityResponseType> {
    return this.http.patch<IRoomAddr>(`${this.resourceUrl}/${getRoomAddrIdentifier(roomAddr) as number}`, roomAddr, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoomAddr>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoomAddr[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRoomAddrToCollectionIfMissing(roomAddrCollection: IRoomAddr[], ...roomAddrsToCheck: (IRoomAddr | null | undefined)[]): IRoomAddr[] {
    const roomAddrs: IRoomAddr[] = roomAddrsToCheck.filter(isPresent);
    if (roomAddrs.length > 0) {
      const roomAddrCollectionIdentifiers = roomAddrCollection.map(roomAddrItem => getRoomAddrIdentifier(roomAddrItem)!);
      const roomAddrsToAdd = roomAddrs.filter(roomAddrItem => {
        const roomAddrIdentifier = getRoomAddrIdentifier(roomAddrItem);
        if (roomAddrIdentifier == null || roomAddrCollectionIdentifiers.includes(roomAddrIdentifier)) {
          return false;
        }
        roomAddrCollectionIdentifiers.push(roomAddrIdentifier);
        return true;
      });
      return [...roomAddrsToAdd, ...roomAddrCollection];
    }
    return roomAddrCollection;
  }
}
