import { Moment } from 'moment';
import { FileType } from 'app/shared/model/enumerations/file-type.model';

export interface IFileinfo {
  id?: number;
  fileContentType?: string;
  file?: any;
  uploaddate?: Moment;
  notes?: string;
  filetype?: FileType;
}

export class Fileinfo implements IFileinfo {
  constructor(
    public id?: number,
    public fileContentType?: string,
    public file?: any,
    public uploaddate?: Moment,
    public notes?: string,
    public filetype?: FileType
  ) {}
}
