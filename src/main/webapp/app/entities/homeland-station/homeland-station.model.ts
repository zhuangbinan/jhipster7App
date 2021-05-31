import { IBuildings } from 'app/entities/buildings/buildings.model';
import { ICommunity } from 'app/entities/community/community.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IHomelandStation {
  id?: number;
  name?: string;
  longCode?: string;
  address?: string | null;
  livingPopulation?: number | null;
  buildingCount?: number | null;
  entranceCount?: number | null;
  logoContentType?: string | null;
  logo?: string | null;
  longitude?: string;
  latitude?: string;
  buildings?: IBuildings[] | null;
  community?: ICommunity | null;
  company?: ICompany | null;
}

export class HomelandStation implements IHomelandStation {
  constructor(
    public id?: number,
    public name?: string,
    public longCode?: string,
    public address?: string | null,
    public livingPopulation?: number | null,
    public buildingCount?: number | null,
    public entranceCount?: number | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public longitude?: string,
    public latitude?: string,
    public buildings?: IBuildings[] | null,
    public community?: ICommunity | null,
    public company?: ICompany | null
  ) {}
}

export function getHomelandStationIdentifier(homelandStation: IHomelandStation): number | undefined {
  return homelandStation.id;
}
