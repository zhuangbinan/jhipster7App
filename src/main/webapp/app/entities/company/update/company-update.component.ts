import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICompany, Company } from '../company.model';
import { CompanyService } from '../service/company.service';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html',
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    longCode: [null, [Validators.required]],
    address: [],
    tel: [],
    email: [],
    managerName: [],
    enable: [],
  });

  constructor(protected companyService: CompanyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      this.updateForm(company);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const company = this.createFromForm();
    if (company.id !== undefined) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
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

  protected updateForm(company: ICompany): void {
    this.editForm.patchValue({
      id: company.id,
      name: company.name,
      longCode: company.longCode,
      address: company.address,
      tel: company.tel,
      email: company.email,
      managerName: company.managerName,
      enable: company.enable,
    });
  }

  protected createFromForm(): ICompany {
    return {
      ...new Company(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      longCode: this.editForm.get(['longCode'])!.value,
      address: this.editForm.get(['address'])!.value,
      tel: this.editForm.get(['tel'])!.value,
      email: this.editForm.get(['email'])!.value,
      managerName: this.editForm.get(['managerName'])!.value,
      enable: this.editForm.get(['enable'])!.value,
    };
  }
}
