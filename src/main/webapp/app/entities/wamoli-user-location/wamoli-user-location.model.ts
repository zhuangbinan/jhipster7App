import * as dayjs from 'dayjs';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';

export interface IWamoliUserLocation {
  id?: number;
  state?: number;
  cardNum?: string;
  expireTime?: dayjs.Dayjs | null;
  delayTime?: number | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  wamoliUser?: IWamoliUser | null;
}

export class WamoliUserLocation implements IWamoliUserLocation {
  constructor(
    public id?: number,
    public state?: number,
    public cardNum?: string,
    public expireTime?: dayjs.Dayjs | null,
    public delayTime?: number | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public wamoliUser?: IWamoliUser | null
  ) {}
}

export function getWamoliUserLocationIdentifier(wamoliUserLocation: IWamoliUserLocation): number | undefined {
  return wamoliUserLocation.id;
}
