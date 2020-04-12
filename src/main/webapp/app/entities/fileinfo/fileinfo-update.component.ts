import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IFileinfo, Fileinfo } from 'app/shared/model/fileinfo.model';
import { FileinfoService } from './fileinfo.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-fileinfo-update',
  templateUrl: './fileinfo-update.component.html'
})
export class FileinfoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    file: [null, [Validators.required]],
    fileContentType: [],
    uploaddate: [null, [Validators.required]],
    notes: [],
    filetype: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected fileinfoService: FileinfoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileinfo }) => {
      this.updateForm(fileinfo);
    });
  }

  updateForm(fileinfo: IFileinfo): void {
    this.editForm.patchValue({
      id: fileinfo.id,
      file: fileinfo.file,
      fileContentType: fileinfo.fileContentType,
      uploaddate: fileinfo.uploaddate != null ? fileinfo.uploaddate.format(DATE_TIME_FORMAT) : null,
      notes: fileinfo.notes,
      filetype: fileinfo.filetype
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('pdfeditorApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileinfo = this.createFromForm();
    if (fileinfo.id !== undefined) {
      this.subscribeToSaveResponse(this.fileinfoService.update(fileinfo));
    } else {
      this.subscribeToSaveResponse(this.fileinfoService.create(fileinfo));
    }
  }

  private createFromForm(): IFileinfo {
    return {
      ...new Fileinfo(),
      id: this.editForm.get(['id'])!.value,
      fileContentType: this.editForm.get(['fileContentType'])!.value,
      file: this.editForm.get(['file'])!.value,
      uploaddate:
        this.editForm.get(['uploaddate'])!.value != null ? moment(this.editForm.get(['uploaddate'])!.value, DATE_TIME_FORMAT) : undefined,
      notes: this.editForm.get(['notes'])!.value,
      filetype: this.editForm.get(['filetype'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileinfo>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
