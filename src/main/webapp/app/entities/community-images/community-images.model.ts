import * as dayjs from 'dayjs';
import { ICommunityImageGroup } from 'app/entities/community-image-group/community-image-group.model';

export interface ICommunityImages {
  id?: number;
  imgContentContentType?: string | null;
  imgContent?: string | null;
  imgTitle?: string | null;
  imgDesc?: string | null;
  orderNum?: number | null;
  lastModifyDate?: dayjs.Dayjs | null;
  lastModifyBy?: string | null;
  communityImageGroup?: ICommunityImageGroup | null;
}

export class CommunityImages implements ICommunityImages {
  constructor(
    public id?: number,
    public imgContentContentType?: string | null,
    public imgContent?: string | null,
    public imgTitle?: string | null,
    public imgDesc?: string | null,
    public orderNum?: number | null,
    public lastModifyDate?: dayjs.Dayjs | null,
    public lastModifyBy?: string | null,
    public communityImageGroup?: ICommunityImageGroup | null
  ) {}
}

export function getCommunityImagesIdentifier(communityImages: ICommunityImages): number | undefined {
  return communityImages.id;
}
