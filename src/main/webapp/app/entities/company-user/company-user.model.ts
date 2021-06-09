import * as dayjs from 'dayjs';

export interface ICompanyUser {
  id?: number;
  userName?: string | null;
  idCardNum?: string | null;
  gender?: string | null;
  phoneNum?: string | null;
  email?: string | null;
  deptName?: string | null;
  postName?: string | null;
  enable?: boolean | null;
  remark?: string | null;
  delFlag?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export class CompanyUser implements ICompanyUser {
  constructor(
    public id?: number,
    public userName?: string | null,
    public idCardNum?: string | null,
    public gender?: string | null,
    public phoneNum?: string | null,
    public email?: string | null,
    public deptName?: string | null,
    public postName?: string | null,
    public enable?: boolean | null,
    public remark?: string | null,
    public delFlag?: boolean | null,
    public createdBy?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null
  ) {
    this.enable = this.enable ?? false;
    this.delFlag = this.delFlag ?? false;
  }
}

export function getCompanyUserIdentifier(companyUser: ICompanyUser): number | undefined {
  return companyUser.id;
}
