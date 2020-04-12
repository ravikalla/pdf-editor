import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PdfeditorTestModule } from '../../../test.module';
import { FileinfoDetailComponent } from 'app/entities/fileinfo/fileinfo-detail.component';
import { Fileinfo } from 'app/shared/model/fileinfo.model';

describe('Component Tests', () => {
  describe('Fileinfo Management Detail Component', () => {
    let comp: FileinfoDetailComponent;
    let fixture: ComponentFixture<FileinfoDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ fileinfo: new Fileinfo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PdfeditorTestModule],
        declarations: [FileinfoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FileinfoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FileinfoDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load fileinfo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fileinfo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
