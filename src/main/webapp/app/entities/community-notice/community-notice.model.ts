import * as dayjs from 'dayjs';
import { ICommunity } from 'app/entities/community/community.model';

export interface ICommunityNotice {
  id?: number;
  title?: string | null;
  img1ContentType?: string | null;
  img1?: string | null;
  img1Title?: string | null;
  content1?: string | null;
  img2ContentType?: string | null;
  img2?: string | null;
  img2Title?: string | null;
  content2?: string | null;
  img3ContentType?: string | null;
  img3?: string | null;
  img3Title?: string | null;
  content3?: string | null;
  img4ContentType?: string | null;
  img4?: string | null;
  img4Title?: string | null;
  content4?: string | null;
  img5ContentType?: string | null;
  img5?: string | null;
  img5Title?: string | null;
  content5?: string | null;
  tail?: string | null;
  enable?: boolean | null;
  delFlag?: boolean | null;
  orderNum?: number | null;
  lastModifyDate?: dayjs.Dayjs | null;
  lastModifyBy?: string | null;
  community?: ICommunity | null;
}

export class CommunityNotice implements ICommunityNotice {
  constructor(
    public id?: number,
    public title?: string | null,
    public img1ContentType?: string | null,
    public img1?: string | null,
    public img1Title?: string | null,
    public content1?: string | null,
    public img2ContentType?: string | null,
    public img2?: string | null,
    public img2Title?: string | null,
    public content2?: string | null,
    public img3ContentType?: string | null,
    public img3?: string | null,
    public img3Title?: string | null,
    public content3?: string | null,
    public img4ContentType?: string | null,
    public img4?: string | null,
    public img4Title?: string | null,
    public content4?: string | null,
    public img5ContentType?: string | null,
    public img5?: string | null,
    public img5Title?: string | null,
    public content5?: string | null,
    public tail?: string | null,
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

export function getCommunityNoticeIdentifier(communityNotice: ICommunityNotice): number | undefined {
  return communityNotice.id;
}
