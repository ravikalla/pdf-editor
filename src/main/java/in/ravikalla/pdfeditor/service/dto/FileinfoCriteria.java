package in.ravikalla.pdfeditor.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import in.ravikalla.pdfeditor.domain.enumeration.FileType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link in.ravikalla.pdfeditor.domain.Fileinfo} entity. This class is used
 * in {@link in.ravikalla.pdfeditor.web.rest.FileinfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fileinfos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FileinfoCriteria implements Serializable, Criteria {
    /**
     * Class for filtering FileType
     */
    public static class FileTypeFilter extends Filter<FileType> {

        public FileTypeFilter() {
        }

        public FileTypeFilter(FileTypeFilter filter) {
            super(filter);
        }

        @Override
        public FileTypeFilter copy() {
            return new FileTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter uploaddate;

    private StringFilter notes;

    private FileTypeFilter filetype;

    public FileinfoCriteria(){
    }

    public FileinfoCriteria(FileinfoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.uploaddate = other.uploaddate == null ? null : other.uploaddate.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.filetype = other.filetype == null ? null : other.filetype.copy();
    }

    @Override
    public FileinfoCriteria copy() {
        return new FileinfoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(ZonedDateTimeFilter uploaddate) {
        this.uploaddate = uploaddate;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public FileTypeFilter getFiletype() {
        return filetype;
    }

    public void setFiletype(FileTypeFilter filetype) {
        this.filetype = filetype;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FileinfoCriteria that = (FileinfoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uploaddate, that.uploaddate) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(filetype, that.filetype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uploaddate,
        notes,
        filetype
        );
    }

    @Override
    public String toString() {
        return "FileinfoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uploaddate != null ? "uploaddate=" + uploaddate + ", " : "") +
                (notes != null ? "notes=" + notes + ", " : "") +
                (filetype != null ? "filetype=" + filetype + ", " : "") +
            "}";
    }

}
