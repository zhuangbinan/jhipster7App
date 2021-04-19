import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBook, Book } from '../book.model';
import { BookService } from '../service/book.service';
import { IAuthor } from 'app/entities/author/author.model';
import { AuthorService } from 'app/entities/author/service/author.service';

@Component({
  selector: 'jhi-book-update',
  templateUrl: './book-update.component.html',
})
export class BookUpdateComponent implements OnInit {
  isSaving = false;

  authorsSharedCollection: IAuthor[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    price: [],
    publisherName: [],
    publisherDate: [],
    author: [],
  });

  constructor(
    protected bookService: BookService,
    protected authorService: AuthorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ book }) => {
      if (book.id === undefined) {
        const today = dayjs().startOf('day');
        book.publisherDate = today;
      }

      this.updateForm(book);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const book = this.createFromForm();
    if (book.id !== undefined) {
      this.subscribeToSaveResponse(this.bookService.update(book));
    } else {
      this.subscribeToSaveResponse(this.bookService.create(book));
    }
  }

  trackAuthorById(index: number, item: IAuthor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBook>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(book: IBook): void {
    this.editForm.patchValue({
      id: book.id,
      title: book.title,
      price: book.price,
      publisherName: book.publisherName,
      publisherDate: book.publisherDate ? book.publisherDate.format(DATE_TIME_FORMAT) : null,
      author: book.author,
    });

    this.authorsSharedCollection = this.authorService.addAuthorToCollectionIfMissing(this.authorsSharedCollection, book.author);
  }

  protected loadRelationshipsOptions(): void {
    this.authorService
      .query()
      .pipe(map((res: HttpResponse<IAuthor[]>) => res.body ?? []))
      .pipe(map((authors: IAuthor[]) => this.authorService.addAuthorToCollectionIfMissing(authors, this.editForm.get('author')!.value)))
      .subscribe((authors: IAuthor[]) => (this.authorsSharedCollection = authors));
  }

  protected createFromForm(): IBook {
    return {
      ...new Book(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      price: this.editForm.get(['price'])!.value,
      publisherName: this.editForm.get(['publisherName'])!.value,
      publisherDate: this.editForm.get(['publisherDate'])!.value
        ? dayjs(this.editForm.get(['publisherDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      author: this.editForm.get(['author'])!.value,
    };
  }
}
