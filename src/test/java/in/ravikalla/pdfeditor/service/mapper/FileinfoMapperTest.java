package in.ravikalla.pdfeditor.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class FileinfoMapperTest {

    private FileinfoMapper fileinfoMapper;

    @BeforeEach
    public void setUp() {
        fileinfoMapper = new FileinfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(fileinfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fileinfoMapper.fromId(null)).isNull();
    }
}
