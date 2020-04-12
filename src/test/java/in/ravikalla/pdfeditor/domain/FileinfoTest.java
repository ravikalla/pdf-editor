package in.ravikalla.pdfeditor.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import in.ravikalla.pdfeditor.web.rest.TestUtil;

public class FileinfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fileinfo.class);
        Fileinfo fileinfo1 = new Fileinfo();
        fileinfo1.setId(1L);
        Fileinfo fileinfo2 = new Fileinfo();
        fileinfo2.setId(fileinfo1.getId());
        assertThat(fileinfo1).isEqualTo(fileinfo2);
        fileinfo2.setId(2L);
        assertThat(fileinfo1).isNotEqualTo(fileinfo2);
        fileinfo1.setId(null);
        assertThat(fileinfo1).isNotEqualTo(fileinfo2);
    }
}
