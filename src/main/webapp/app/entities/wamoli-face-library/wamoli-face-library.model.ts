import { IWamoliUser } from 'app/entities/wamoli-user/wamoli-user.model';

export interface IWamoliFaceLibrary {
  id?: number;
  content?: string;
  wamoliUser?: IWamoliUser | null;
}

export class WamoliFaceLibrary implements IWamoliFaceLibrary {
  constructor(public id?: number, public content?: string, public wamoliUser?: IWamoliUser | null) {}
}

export function getWamoliFaceLibraryIdentifier(wamoliFaceLibrary: IWamoliFaceLibrary): number | undefined {
  return wamoliFaceLibrary.id;
}
