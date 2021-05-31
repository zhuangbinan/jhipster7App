import * as dayjs from 'dayjs';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';

export interface IVisitor {
  id?: number;
  name?: string;
  phoneNum?: string;
  carPlateNum?: string | null;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs;
  passwd?: string | null;
  faceImageContentType?: string | null;
  faceImage?: string | null;
  whichEntrance?: string | null;
  roomAddr?: IRoomAddr | null;
}

export class Visitor implements IVisitor {
  constructor(
    public id?: number,
    public name?: string,
    public phoneNum?: string,
    public carPlateNum?: string | null,
    public startTime?: dayjs.Dayjs,
    public endTime?: dayjs.Dayjs,
    public passwd?: string | null,
    public faceImageContentType?: string | null,
    public faceImage?: string | null,
    public whichEntrance?: string | null,
    public roomAddr?: IRoomAddr | null
  ) {}
}

export function getVisitorIdentifier(visitor: IVisitor): number | undefined {
  return visitor.id;
}
