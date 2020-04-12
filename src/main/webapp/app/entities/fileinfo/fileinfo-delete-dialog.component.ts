import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFileinfo } from 'app/shared/model/fileinfo.model';
import { FileinfoService } from './fileinfo.service';

@Component({
  templateUrl: './fileinfo-delete-dialog.component.html'
})
export class FileinfoDeleteDialogComponent {
  fileinfo?: IFileinfo;

  constructor(protected fileinfoService: FileinfoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileinfoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fileinfoListModification');
      this.activeModal.close();
    });
  }
}
