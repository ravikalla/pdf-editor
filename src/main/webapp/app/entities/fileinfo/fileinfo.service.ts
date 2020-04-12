import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFileinfo } from 'app/shared/model/fileinfo.model';

type EntityResponseType = HttpResponse<IFileinfo>;
type EntityArrayResponseType = HttpResponse<IFileinfo[]>;

@Injectable({ providedIn: 'root' })
export class FileinfoService {
  public resourceUrl = SERVER_API_URL + 'api/fileinfos';

  constructor(protected http: HttpClient) {}

  create(fileinfo: IFileinfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileinfo);
    return this.http
      .post<IFileinfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fileinfo: IFileinfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileinfo);
    return this.http
      .put<IFileinfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFileinfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFileinfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(fileinfo: IFileinfo): IFileinfo {
    const copy: IFileinfo = Object.assign({}, fileinfo, {
      uploaddate: fileinfo.uploaddate && fileinfo.uploaddate.isValid() ? fileinfo.uploaddate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.uploaddate = res.body.uploaddate ? moment(res.body.uploaddate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fileinfo: IFileinfo) => {
        fileinfo.uploaddate = fileinfo.uploaddate ? moment(fileinfo.uploaddate) : undefined;
      });
    }
    return res;
  }
}
