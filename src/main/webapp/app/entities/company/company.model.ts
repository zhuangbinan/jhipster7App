import { IHomelandStation } from 'app/entities/homeland-station/homeland-station.model';

export interface ICompany {
  id?: number;
  name?: string;
  longCode?: string;
  address?: string | null;
  tel?: string | null;
  email?: string | null;
  managerName?: string | null;
  enable?: boolean | null;
  homelandStations?: IHomelandStation[] | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public name?: string,
    public longCode?: string,
    public address?: string | null,
    public tel?: string | null,
    public email?: string | null,
    public managerName?: string | null,
    public enable?: boolean | null,
    public homelandStations?: IHomelandStation[] | null
  ) {
    this.enable = this.enable ?? false;
  }
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
