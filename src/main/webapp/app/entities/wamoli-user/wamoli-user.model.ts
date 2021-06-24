import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IRoomAddr } from 'app/entities/room-addr/room-addr.model';
import { CertificateType } from 'app/entities/enumerations/certificate-type.model';
import { UserType } from 'app/entities/enumerations/user-type.model';

export interface IWamoliUser {
  id?: number;
  userName?: string;
  gender?: string | null;
  phoneNum?: string;
  email?: string | null;
  unitAddr?: string | null;
  roomAddr?: number | null;
  idCardNum?: string | null;
  idCardType?: CertificateType | null;
  userType?: UserType | null;
  enable?: boolean | null;
  isManager?: boolean | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  roomAddrs?: IRoomAddr[] | null;
}

export class WamoliUser implements IWamoliUser {
  constructor(
    public id?: number,
    public userName?: string,
    public gender?: string | null,
    public phoneNum?: string,
    public email?: string | null,
    public unitAddr?: string | null,
    public roomAddr?: number | null,
    public idCardNum?: string | null,
    public idCardType?: CertificateType | null,
    public userType?: UserType | null,
    public enable?: boolean | null,
    public isManager?: boolean | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public roomAddrs?: IRoomAddr[] | null
  ) {
    this.enable = this.enable ?? false;
    this.isManager = this.isManager ?? false;
  }
}

export function getWamoliUserIdentifier(wamoliUser: IWamoliUser): number | undefined {
  return wamoliUser.id;
}
