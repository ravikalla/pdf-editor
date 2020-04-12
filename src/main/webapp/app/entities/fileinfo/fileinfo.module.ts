import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PdfeditorSharedModule } from 'app/shared/shared.module';
import { FileinfoComponent } from './fileinfo.component';
import { FileinfoDetailComponent } from './fileinfo-detail.component';
import { FileinfoUpdateComponent } from './fileinfo-update.component';
import { FileinfoDeleteDialogComponent } from './fileinfo-delete-dialog.component';
import { fileinfoRoute } from './fileinfo.route';

@NgModule({
  imports: [PdfeditorSharedModule, RouterModule.forChild(fileinfoRoute)],
  declarations: [FileinfoComponent, FileinfoDetailComponent, FileinfoUpdateComponent, FileinfoDeleteDialogComponent],
  entryComponents: [FileinfoDeleteDialogComponent]
})
export class PdfeditorFileinfoModule {}
