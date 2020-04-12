package in.ravikalla.pdfeditor.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import in.ravikalla.pdfeditor.web.rest.TestUtil;

public class FileinfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileinfoDTO.class);
        FileinfoDTO fileinfoDTO1 = new FileinfoDTO();
        fileinfoDTO1.setId(1L);
        FileinfoDTO fileinfoDTO2 = new FileinfoDTO();
        assertThat(fileinfoDTO1).isNotEqualTo(fileinfoDTO2);
        fileinfoDTO2.setId(fileinfoDTO1.getId());
        assertThat(fileinfoDTO1).isEqualTo(fileinfoDTO2);
        fileinfoDTO2.setId(2L);
        assertThat(fileinfoDTO1).isNotEqualTo(fileinfoDTO2);
        fileinfoDTO1.setId(null);
        assertThat(fileinfoDTO1).isNotEqualTo(fileinfoDTO2);
    }
}
