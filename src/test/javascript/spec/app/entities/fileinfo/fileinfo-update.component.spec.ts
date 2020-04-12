import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PdfeditorTestModule } from '../../../test.module';
import { FileinfoUpdateComponent } from 'app/entities/fileinfo/fileinfo-update.component';
import { FileinfoService } from 'app/entities/fileinfo/fileinfo.service';
import { Fileinfo } from 'app/shared/model/fileinfo.model';

describe('Component Tests', () => {
  describe('Fileinfo Management Update Component', () => {
    let comp: FileinfoUpdateComponent;
    let fixture: ComponentFixture<FileinfoUpdateComponent>;
    let service: FileinfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PdfeditorTestModule],
        declarations: [FileinfoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FileinfoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileinfoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FileinfoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Fileinfo(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Fileinfo();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
