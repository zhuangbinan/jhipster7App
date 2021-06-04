import * as dayjs from 'dayjs';
import { ICommunityLeader } from 'app/entities/community-leader/community-leader.model';
import { ICommunityNotice } from 'app/entities/community-notice/community-notice.model';
import { IHomelandStation } from 'app/entities/homeland-station/homeland-station.model';

export interface ICommunity {
  id?: number;
  cname?: string | null;
  managerName?: string | null;
  address?: string | null;
  tel?: string | null;
  email?: string | null;
  officeHours?: string | null;
  description?: string | null;
  source?: string | null;
  parentId?: number | null;
  ancestors?: string | null;
  longCode?: string | null;
  enable?: boolean | null;
  delFlag?: boolean | null;
  orderNum?: number | null;
  lastModifyDate?: dayjs.Dayjs | null;
  lastModifyBy?: string | null;
  communityLeaders?: ICommunityLeader[] | null;
  communityNotices?: ICommunityNotice[] | null;
  homelandStations?: IHomelandStation[] | null;
}

export class Community implements ICommunity {
  constructor(
    public id?: number,
    public cname?: string | null,
    public managerName?: string | null,
    public address?: string | null,
    public tel?: string | null,
    public email?: string | null,
    public officeHours?: string | null,
    public description?: string | null,
    public source?: string | null,
    public parentId?: number | null,
    public ancestors?: string | null,
    public longCode?: string | null,
    public enable?: boolean | null,
    public delFlag?: boolean | null,
    public orderNum?: number | null,
    public lastModifyDate?: dayjs.Dayjs | null,
    public lastModifyBy?: string | null,
    public communityLeaders?: ICommunityLeader[] | null,
    public communityNotices?: ICommunityNotice[] | null,
    public homelandStations?: IHomelandStation[] | null
  ) {
    this.enable = this.enable ?? false;
    this.delFlag = this.delFlag ?? false;
  }
}

export function getCommunityIdentifier(community: ICommunity): number | undefined {
  return community.id;
}
