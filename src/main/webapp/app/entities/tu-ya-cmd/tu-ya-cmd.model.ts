import * as dayjs from 'dayjs';
import { ITuYaDevice } from 'app/entities/tu-ya-device/tu-ya-device.model';
import { CmdType } from 'app/entities/enumerations/cmd-type.model';

export interface ITuYaCmd {
  id?: number;
  cmdName?: string | null;
  cmdCode?: string | null;
  value?: boolean | null;
  cmdType?: CmdType | null;
  createTime?: dayjs.Dayjs | null;
  enable?: boolean | null;
  tuYaDevice?: ITuYaDevice | null;
}

export class TuYaCmd implements ITuYaCmd {
  constructor(
    public id?: number,
    public cmdName?: string | null,
    public cmdCode?: string | null,
    public value?: boolean | null,
    public cmdType?: CmdType | null,
    public createTime?: dayjs.Dayjs | null,
    public enable?: boolean | null,
    public tuYaDevice?: ITuYaDevice | null
  ) {
    this.value = this.value ?? false;
    this.enable = this.enable ?? false;
  }
}

export function getTuYaCmdIdentifier(tuYaCmd: ITuYaCmd): number | undefined {
  return tuYaCmd.id;
}
