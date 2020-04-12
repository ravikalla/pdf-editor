package in.ravikalla.pdfeditor.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import in.ravikalla.pdfeditor.domain.enumeration.FileType;

/**
 * A Fileinfo.
 */
@Entity
@Table(name = "fileinfo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fileinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Lob
    @Column(name = "file", nullable = false)
    private byte[] file;

    @Column(name = "file_content_type", nullable = false)
    private String fileContentType;

    @NotNull
    @Column(name = "uploaddate", nullable = false)
    private ZonedDateTime uploaddate;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "filetype")
    private FileType filetype;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public Fileinfo file(byte[] file) {
        this.file = file;
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public Fileinfo fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public ZonedDateTime getUploaddate() {
        return uploaddate;
    }

    public Fileinfo uploaddate(ZonedDateTime uploaddate) {
        this.uploaddate = uploaddate;
        return this;
    }

    public void setUploaddate(ZonedDateTime uploaddate) {
        this.uploaddate = uploaddate;
    }

    public String getNotes() {
        return notes;
    }

    public Fileinfo notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public FileType getFiletype() {
        return filetype;
    }

    public Fileinfo filetype(FileType filetype) {
        this.filetype = filetype;
        return this;
    }

    public void setFiletype(FileType filetype) {
        this.filetype = filetype;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fileinfo)) {
            return false;
        }
        return id != null && id.equals(((Fileinfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Fileinfo{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", uploaddate='" + getUploaddate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", filetype='" + getFiletype() + "'" +
            "}";
    }
}
