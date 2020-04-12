import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PdfeditorSharedModule } from 'app/shared/shared.module';
import { PdfeditorCoreModule } from 'app/core/core.module';
import { PdfeditorAppRoutingModule } from './app-routing.module';
import { PdfeditorHomeModule } from './home/home.module';
import { PdfeditorEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PdfeditorSharedModule,
    PdfeditorCoreModule,
    PdfeditorHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PdfeditorEntityModule,
    PdfeditorAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class PdfeditorAppModule {}
