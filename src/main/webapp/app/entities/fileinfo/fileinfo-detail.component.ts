import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IFileinfo } from 'app/shared/model/fileinfo.model';

@Component({
  selector: 'jhi-fileinfo-detail',
  templateUrl: './fileinfo-detail.component.html'
})
export class FileinfoDetailComponent implements OnInit {
  fileinfo: IFileinfo | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileinfo }) => {
      this.fileinfo = fileinfo;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
