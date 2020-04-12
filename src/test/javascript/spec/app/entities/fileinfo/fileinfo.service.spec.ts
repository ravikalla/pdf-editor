import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { FileinfoService } from 'app/entities/fileinfo/fileinfo.service';
import { IFileinfo, Fileinfo } from 'app/shared/model/fileinfo.model';
import { FileType } from 'app/shared/model/enumerations/file-type.model';

describe('Service Tests', () => {
  describe('Fileinfo Service', () => {
    let injector: TestBed;
    let service: FileinfoService;
    let httpMock: HttpTestingController;
    let elemDefault: IFileinfo;
    let expectedResult: IFileinfo | IFileinfo[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FileinfoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Fileinfo(0, 'image/png', 'AAAAAAA', currentDate, 'AAAAAAA', FileType.PDF);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            uploaddate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Fileinfo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            uploaddate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            uploaddate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Fileinfo())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Fileinfo', () => {
        const returnedFromService = Object.assign(
          {
            file: 'BBBBBB',
            uploaddate: currentDate.format(DATE_TIME_FORMAT),
            notes: 'BBBBBB',
            filetype: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            uploaddate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Fileinfo', () => {
        const returnedFromService = Object.assign(
          {
            file: 'BBBBBB',
            uploaddate: currentDate.format(DATE_TIME_FORMAT),
            notes: 'BBBBBB',
            filetype: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            uploaddate: currentDate
          },
          returnedFromService
        );
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Fileinfo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
