package in.ravikalla.pdfeditor.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import in.ravikalla.pdfeditor.domain.enumeration.FileType;

/**
 * A DTO for the {@link in.ravikalla.pdfeditor.domain.Fileinfo} entity.
 */
public class FileinfoDTO implements Serializable {

    private Long id;

    
    @Lob
    private byte[] file;

    private String fileContentType;
    @NotNull
    private ZonedDateTime uploaddate;

    private String notes;

    private FileType filetype;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public ZonedDateTime getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(ZonedDateTime uploaddate) {
        this.uploaddate = uploaddate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public FileType getFiletype() {
        return filetype;
    }

    public void setFiletype(FileType filetype) {
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

        FileinfoDTO fileinfoDTO = (FileinfoDTO) o;
        if (fileinfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fileinfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FileinfoDTO{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", uploaddate='" + getUploaddate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", filetype='" + getFiletype() + "'" +
            "}";
    }
}
