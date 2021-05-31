import { IVisitor } from 'app/entities/visitor/visitor.model';
import { IBuildings } from 'app/entities/buildings/buildings.model';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';

export interface IRoomAddr {
  id?: number;
  roomNum?: string;
  unit?: string | null;
  roomType?: string | null;
  roomArea?: number | null;
  used?: boolean | null;
  autoControl?: boolean | null;
  longCode?: string;
  visitors?: IVisitor[] | null;
  buildings?: IBuildings | null;
  wamoliUsers?: IWamoliUser[] | null;
}

export class RoomAddr implements IRoomAddr {
  constructor(
    public id?: number,
    public roomNum?: string,
    public unit?: string | null,
    public roomType?: string | null,
    public roomArea?: number | null,
    public used?: boolean | null,
    public autoControl?: boolean | null,
    public longCode?: string,
    public visitors?: IVisitor[] | null,
    public buildings?: IBuildings | null,
    public wamoliUsers?: IWamoliUser[] | null
  ) {
    this.used = this.used ?? false;
    this.autoControl = this.autoControl ?? false;
  }
}

export function getRoomAddrIdentifier(roomAddr: IRoomAddr): number | undefined {
  return roomAddr.id;
}
