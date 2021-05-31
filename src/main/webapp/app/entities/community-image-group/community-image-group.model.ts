import * as dayjs from 'dayjs';
import { ICommunityImages } from 'app/entities/community-images/community-images.model';

export interface ICommunityImageGroup {
  id?: number;
  imgGroupName?: string | null;
  orderNum?: number | null;
  lastModifyDate?: dayjs.Dayjs | null;
  lastModifyBy?: string | null;
  communityImages?: ICommunityImages[] | null;
}

export class CommunityImageGroup implements ICommunityImageGroup {
  constructor(
    public id?: number,
    public imgGroupName?: string | null,
    public orderNum?: number | null,
    public lastModifyDate?: dayjs.Dayjs | null,
    public lastModifyBy?: string | null,
    public communityImages?: ICommunityImages[] | null
  ) {}
}

export function getCommunityImageGroupIdentifier(communityImageGroup: ICommunityImageGroup): number | undefined {
  return communityImageGroup.id;
}
