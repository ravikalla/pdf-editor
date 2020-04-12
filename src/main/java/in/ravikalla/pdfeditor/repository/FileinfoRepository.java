package in.ravikalla.pdfeditor.repository;

import in.ravikalla.pdfeditor.domain.Fileinfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Fileinfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileinfoRepository extends JpaRepository<Fileinfo, Long>, JpaSpecificationExecutor<Fileinfo> {

}
