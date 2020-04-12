import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'fileinfo',
        loadChildren: () => import('./fileinfo/fileinfo.module').then(m => m.PdfeditorFileinfoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PdfeditorEntityModule {}
