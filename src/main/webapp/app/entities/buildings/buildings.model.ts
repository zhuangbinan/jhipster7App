import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';
import { IHomelandStation } from 'app/entities/homeland-station/homeland-station.model';

export interface IBuildings {
  id?: number;
  name?: string | null;
  longCode?: string;
  floorCount?: number | null;
  unites?: number | null;
  longitude?: string;
  latitude?: string;
  enable?: boolean | null;
  roomAddrs?: IRoomAddr[] | null;
  homelandStation?: IHomelandStation | null;
}

export class Buildings implements IBuildings {
  constructor(
    public id?: number,
    public name?: string | null,
    public longCode?: string,
    public floorCount?: number | null,
    public unites?: number | null,
    public longitude?: string,
    public latitude?: string,
    public enable?: boolean | null,
    public roomAddrs?: IRoomAddr[] | null,
    public homelandStation?: IHomelandStation | null
  ) {
    this.enable = this.enable ?? false;
  }
}

export function getBuildingsIdentifier(buildings: IBuildings): number | undefined {
  return buildings.id;
}
