import * as dayjs from 'dayjs';
import { ITuYaCmd } from 'app/entities/tu-ya-cmd/tu-ya-cmd.model';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';

export interface ITuYaDevice {
  id?: number;
  deviceName?: string | null;
  longCode?: number | null;
  tyDeviceId?: string | null;
  deviceType?: number | null;
  cmdCode?: string | null;
  createTime?: dayjs.Dayjs | null;
  enable?: boolean | null;
  tuYaCmds?: ITuYaCmd[] | null;
  roomAddr?: IRoomAddr | null;
}

export class TuYaDevice implements ITuYaDevice {
  constructor(
    public id?: number,
    public deviceName?: string | null,
    public longCode?: number | null,
    public tyDeviceId?: string | null,
    public deviceType?: number | null,
    public cmdCode?: string | null,
    public createTime?: dayjs.Dayjs | null,
    public enable?: boolean | null,
    public tuYaCmds?: ITuYaCmd[] | null,
    public roomAddr?: IRoomAddr | null
  ) {
    this.enable = this.enable ?? false;
  }
}

export function getTuYaDeviceIdentifier(tuYaDevice: ITuYaDevice): number | undefined {
  return tuYaDevice.id;
}
