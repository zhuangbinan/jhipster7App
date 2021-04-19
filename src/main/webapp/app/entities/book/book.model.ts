import * as dayjs from 'dayjs';
import { IAuthor } from 'app/entities/author/author.model';

export interface IBook {
  id?: number;
  title?: string | null;
  price?: number | null;
  publisherName?: string | null;
  publisherDate?: dayjs.Dayjs | null;
  author?: IAuthor | null;
}

export class Book implements IBook {
  constructor(
    public id?: number,
    public title?: string | null,
    public price?: number | null,
    public publisherName?: string | null,
    public publisherDate?: dayjs.Dayjs | null,
    public author?: IAuthor | null
  ) {}
}

export function getBookIdentifier(book: IBook): number | undefined {
  return book.id;
}
