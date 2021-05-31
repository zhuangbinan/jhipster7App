import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHomelandStation, getHomelandStationIdentifier } from '../homeland-station.model';

export type EntityResponseType = HttpResponse<IHomelandStation>;
export type EntityArrayResponseType = HttpResponse<IHomelandStation[]>;

@Injectable({ providedIn: 'root' })
export class HomelandStationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/homeland-stations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(homelandStation: IHomelandStation): Observable<EntityResponseType> {
    return this.http.post<IHomelandStation>(this.resourceUrl, homelandStation, { observe: 'response' });
  }

  update(homelandStation: IHomelandStation): Observable<EntityResponseType> {
    return this.http.put<IHomelandStation>(
      `${this.resourceUrl}/${getHomelandStationIdentifier(homelandStation) as number}`,
      homelandStation,
      { observe: 'response' }
    );
  }

  partialUpdate(homelandStation: IHomelandStation): Observable<EntityResponseType> {
    return this.http.patch<IHomelandStation>(
      `${this.resourceUrl}/${getHomelandStationIdentifier(homelandStation) as number}`,
      homelandStation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHomelandStation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHomelandStation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHomelandStationToCollectionIfMissing(
    homelandStationCollection: IHomelandStation[],
    ...homelandStationsToCheck: (IHomelandStation | null | undefined)[]
  ): IHomelandStation[] {
    const homelandStations: IHomelandStation[] = homelandStationsToCheck.filter(isPresent);
    if (homelandStations.length > 0) {
      const homelandStationCollectionIdentifiers = homelandStationCollection.map(
        homelandStationItem => getHomelandStationIdentifier(homelandStationItem)!
      );
      const homelandStationsToAdd = homelandStations.filter(homelandStationItem => {
        const homelandStationIdentifier = getHomelandStationIdentifier(homelandStationItem);
        if (homelandStationIdentifier == null || homelandStationCollectionIdentifiers.includes(homelandStationIdentifier)) {
          return false;
        }
        homelandStationCollectionIdentifiers.push(homelandStationIdentifier);
        return true;
      });
      return [...homelandStationsToAdd, ...homelandStationCollection];
    }
    return homelandStationCollection;
  }
}
