import * as dayjs from 'dayjs';
import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';

export interface ICompanyDept {
  id?: number;
  parentId?: number | null;
  ancestors?: string | null;
  deptName?: string | null;
  orderNum?: number | null;
  leaderName?: string | null;
  tel?: string | null;
  email?: string | null;
  enable?: boolean | null;
  delFlag?: boolean | null;
  createBy?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModifyBy?: string | null;
  lastModifyDate?: dayjs.Dayjs | null;
  wamoliUsers?: IWamoliUser[] | null;
}

export class CompanyDept implements ICompanyDept {
  constructor(
    public id?: number,
    public parentId?: number | null,
    public ancestors?: string | null,
    public deptName?: string | null,
    public orderNum?: number | null,
    public leaderName?: string | null,
    public tel?: string | null,
    public email?: string | null,
    public enable?: boolean | null,
    public delFlag?: boolean | null,
    public createBy?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModifyBy?: string | null,
    public lastModifyDate?: dayjs.Dayjs | null,
    public wamoliUsers?: IWamoliUser[] | null
  ) {
    this.enable = this.enable ?? false;
    this.delFlag = this.delFlag ?? false;
  }
}

export function getCompanyDeptIdentifier(companyDept: ICompanyDept): number | undefined {
  return companyDept.id;
}
