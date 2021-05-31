import * as dayjs from 'dayjs';
import { ICompanyDept } from 'app/entities/company-dept/company-dept.model';

export interface ICompanyPost {
  id?: number;
  postCode?: string | null;
  postName?: string | null;
  orderNum?: number | null;
  remark?: string | null;
  enable?: boolean | null;
  createBy?: string | null;
  createDate?: dayjs.Dayjs | null;
  lastModifyBy?: string | null;
  lastModifyDate?: dayjs.Dayjs | null;
  companyDept?: ICompanyDept | null;
}

export class CompanyPost implements ICompanyPost {
  constructor(
    public id?: number,
    public postCode?: string | null,
    public postName?: string | null,
    public orderNum?: number | null,
    public remark?: string | null,
    public enable?: boolean | null,
    public createBy?: string | null,
    public createDate?: dayjs.Dayjs | null,
    public lastModifyBy?: string | null,
    public lastModifyDate?: dayjs.Dayjs | null,
    public companyDept?: ICompanyDept | null
  ) {
    this.enable = this.enable ?? false;
  }
}

export function getCompanyPostIdentifier(companyPost: ICompanyPost): number | undefined {
  return companyPost.id;
}
