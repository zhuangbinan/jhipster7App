export interface ITest01 {
  id?: number;
  jobCareerDesc?: string | null;
}

export class Test01 implements ITest01 {
  constructor(public id?: number, public jobCareerDesc?: string | null) {}
}

export function getTest01Identifier(test01: ITest01): number | undefined {
  return test01.id;
}
