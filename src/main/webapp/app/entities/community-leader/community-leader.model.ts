import * as dayjs from 'dayjs';
import { ICommunity } from 'app/entities/community/community.model';

export interface ICommunityLeader {
  id?: number;
  avatarContentType?: string | null;
  avatar?: string | null;
  realName?: string | null;
  tel?: string | null;
  jobTitle?: string | null;
  jobDesc?: string | null;
  jobCareerDesc?: string | null;
  enable?: boolean | null;
  delFlag?: boolean | null;
  orderNum?: number | null;
  lastModifyDate?: dayjs.Dayjs | null;
  lastModifyBy?: string | null;
  community?: ICommunity | null;
}

export class CommunityLeader implements ICommunityLeader {
  constructor(
    public id?: number,
    public avatarContentType?: string | null,
    public avatar?: string | null,
    public realName?: string | null,
    public tel?: string | null,
    public jobTitle?: string | null,
    public jobDesc?: string | null,
    public jobCareerDesc?: string | null,
    public enable?: boolean | null,
    public delFlag?: boolean | null,
    public orderNum?: number | null,
    public lastModifyDate?: dayjs.Dayjs | null,
    public lastModifyBy?: string | null,
    public community?: ICommunity | null
  ) {
    this.enable = this.enable ?? false;
    this.delFlag = this.delFlag ?? false;
  }
}

export function getCommunityLeaderIdentifier(communityLeader: ICommunityLeader): number | undefined {
  return communityLeader.id;
}
