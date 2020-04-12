package in.ravikalla.pdfeditor.service.mapper;

import in.ravikalla.pdfeditor.domain.*;
import in.ravikalla.pdfeditor.service.dto.FileinfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fileinfo} and its DTO {@link FileinfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileinfoMapper extends EntityMapper<FileinfoDTO, Fileinfo> {



    default Fileinfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Fileinfo fileinfo = new Fileinfo();
        fileinfo.setId(id);
        return fileinfo;
    }
}
