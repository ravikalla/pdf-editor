import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileinfo } from 'app/shared/model/fileinfo.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FileinfoService } from './fileinfo.service';
import { FileinfoDeleteDialogComponent } from './fileinfo-delete-dialog.component';

@Component({
  selector: 'jhi-fileinfo',
  templateUrl: './fileinfo.component.html'
})
export class FileinfoComponent implements OnInit, OnDestroy {
  fileinfos: IFileinfo[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected fileinfoService: FileinfoService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.fileinfos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.fileinfoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFileinfo[]>) => this.paginateFileinfos(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.fileinfos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFileinfos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFileinfo): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInFileinfos(): void {
    this.eventSubscriber = this.eventManager.subscribe('fileinfoListModification', () => this.reset());
  }

  delete(fileinfo: IFileinfo): void {
    const modalRef = this.modalService.open(FileinfoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fileinfo = fileinfo;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFileinfos(data: IFileinfo[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.fileinfos.push(data[i]);
      }
    }
  }
}
