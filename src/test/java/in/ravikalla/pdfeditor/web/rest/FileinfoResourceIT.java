package in.ravikalla.pdfeditor.web.rest;

import in.ravikalla.pdfeditor.PdfeditorApp;
import in.ravikalla.pdfeditor.domain.Fileinfo;
import in.ravikalla.pdfeditor.repository.FileinfoRepository;
import in.ravikalla.pdfeditor.service.FileinfoService;
import in.ravikalla.pdfeditor.service.dto.FileinfoDTO;
import in.ravikalla.pdfeditor.service.mapper.FileinfoMapper;
import in.ravikalla.pdfeditor.web.rest.errors.ExceptionTranslator;
import in.ravikalla.pdfeditor.service.dto.FileinfoCriteria;
import in.ravikalla.pdfeditor.service.FileinfoQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static in.ravikalla.pdfeditor.web.rest.TestUtil.sameInstant;
import static in.ravikalla.pdfeditor.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import in.ravikalla.pdfeditor.domain.enumeration.FileType;
/**
 * Integration tests for the {@link FileinfoResource} REST controller.
 */
@SpringBootTest(classes = PdfeditorApp.class)
public class FileinfoResourceIT {

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_UPLOADDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPLOADDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPLOADDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final FileType DEFAULT_FILETYPE = FileType.PDF;
    private static final FileType UPDATED_FILETYPE = FileType.TXT;

    @Autowired
    private FileinfoRepository fileinfoRepository;

    @Autowired
    private FileinfoMapper fileinfoMapper;

    @Autowired
    private FileinfoService fileinfoService;

    @Autowired
    private FileinfoQueryService fileinfoQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFileinfoMockMvc;

    private Fileinfo fileinfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileinfoResource fileinfoResource = new FileinfoResource(fileinfoService, fileinfoQueryService);
        this.restFileinfoMockMvc = MockMvcBuilders.standaloneSetup(fileinfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fileinfo createEntity(EntityManager em) {
        Fileinfo fileinfo = new Fileinfo()
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .uploaddate(DEFAULT_UPLOADDATE)
            .notes(DEFAULT_NOTES)
            .filetype(DEFAULT_FILETYPE);
        return fileinfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fileinfo createUpdatedEntity(EntityManager em) {
        Fileinfo fileinfo = new Fileinfo()
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .uploaddate(UPDATED_UPLOADDATE)
            .notes(UPDATED_NOTES)
            .filetype(UPDATED_FILETYPE);
        return fileinfo;
    }

    @BeforeEach
    public void initTest() {
        fileinfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileinfo() throws Exception {
        int databaseSizeBeforeCreate = fileinfoRepository.findAll().size();

        // Create the Fileinfo
        FileinfoDTO fileinfoDTO = fileinfoMapper.toDto(fileinfo);
        restFileinfoMockMvc.perform(post("/api/fileinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileinfoDTO)))
            .andExpect(status().isCreated());

        // Validate the Fileinfo in the database
        List<Fileinfo> fileinfoList = fileinfoRepository.findAll();
        assertThat(fileinfoList).hasSize(databaseSizeBeforeCreate + 1);
        Fileinfo testFileinfo = fileinfoList.get(fileinfoList.size() - 1);
        assertThat(testFileinfo.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testFileinfo.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testFileinfo.getUploaddate()).isEqualTo(DEFAULT_UPLOADDATE);
        assertThat(testFileinfo.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testFileinfo.getFiletype()).isEqualTo(DEFAULT_FILETYPE);
    }

    @Test
    @Transactional
    public void createFileinfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileinfoRepository.findAll().size();

        // Create the Fileinfo with an existing ID
        fileinfo.setId(1L);
        FileinfoDTO fileinfoDTO = fileinfoMapper.toDto(fileinfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileinfoMockMvc.perform(post("/api/fileinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileinfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fileinfo in the database
        List<Fileinfo> fileinfoList = fileinfoRepository.findAll();
        assertThat(fileinfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUploaddateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileinfoRepository.findAll().size();
        // set the field null
        fileinfo.setUploaddate(null);

        // Create the Fileinfo, which fails.
        FileinfoDTO fileinfoDTO = fileinfoMapper.toDto(fileinfo);

        restFileinfoMockMvc.perform(post("/api/fileinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileinfoDTO)))
            .andExpect(status().isBadRequest());

        List<Fileinfo> fileinfoList = fileinfoRepository.findAll();
        assertThat(fileinfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFileinfos() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList
        restFileinfoMockMvc.perform(get("/api/fileinfos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileinfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].uploaddate").value(hasItem(sameInstant(DEFAULT_UPLOADDATE))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].filetype").value(hasItem(DEFAULT_FILETYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getFileinfo() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get the fileinfo
        restFileinfoMockMvc.perform(get("/api/fileinfos/{id}", fileinfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fileinfo.getId().intValue()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.uploaddate").value(sameInstant(DEFAULT_UPLOADDATE)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.filetype").value(DEFAULT_FILETYPE.toString()));
    }


    @Test
    @Transactional
    public void getFileinfosByIdFiltering() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        Long id = fileinfo.getId();

        defaultFileinfoShouldBeFound("id.equals=" + id);
        defaultFileinfoShouldNotBeFound("id.notEquals=" + id);

        defaultFileinfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFileinfoShouldNotBeFound("id.greaterThan=" + id);

        defaultFileinfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFileinfoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFileinfosByUploaddateIsEqualToSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where uploaddate equals to DEFAULT_UPLOADDATE
        defaultFileinfoShouldBeFound("uploaddate.equals=" + DEFAULT_UPLOADDATE);

        // Get all the fileinfoList where uploaddate equals to UPDATED_UPLOADDATE
        defaultFileinfoShouldNotBeFound("uploaddate.equals=" + UPDATED_UPLOADDATE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByUploaddateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where uploaddate not equals to DEFAULT_UPLOADDATE
        defaultFileinfoShouldNotBeFound("uploaddate.notEquals=" + DEFAULT_UPLOADDATE);

        // Get all the fileinfoList where uploaddate not equals to UPDATED_UPLOADDATE
        defaultFileinfoShouldBeFound("uploaddate.notEquals=" + UPDATED_UPLOADDATE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByUploaddateIsInShouldWork() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where uploaddate in DEFAULT_UPLOADDATE or UPDATED_UPLOADDATE
        defaultFileinfoShouldBeFound("uploaddate.in=" + DEFAULT_UPLOADDATE + "," + UPDATED_UPLOADDATE);

        // Get all the fileinfoList where uploaddate equals to UPDATED_UPLOADDATE
        defaultFileinfoShouldNotBeFound("uploaddate.in=" + UPDATED_UPLOADDATE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByUploaddateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where uploaddate is not null
        defaultFileinfoShouldBeFound("uploaddate.specified=true");

        // Get all the fileinfoList where uploaddate is null
        defaultFileinfoShouldNotBeFound("uploaddate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFileinfosByUploaddateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where uploaddate is greater than or equal to DEFAULT_UPLOADDATE
        defaultFileinfoShouldBeFound("uploaddate.greaterThanOrEqual=" + DEFAULT_UPLOADDATE);

        // Get all the fileinfoList where uploaddate is greater than or equal to UPDATED_UPLOADDATE
        defaultFileinfoShouldNotBeFound("uploaddate.greaterThanOrEqual=" + UPDATED_UPLOADDATE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByUploaddateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where uploaddate is less than or equal to DEFAULT_UPLOADDATE
        defaultFileinfoShouldBeFound("uploaddate.lessThanOrEqual=" + DEFAULT_UPLOADDATE);

        // Get all the fileinfoList where uploaddate is less than or equal to SMALLER_UPLOADDATE
        defaultFileinfoShouldNotBeFound("uploaddate.lessThanOrEqual=" + SMALLER_UPLOADDATE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByUploaddateIsLessThanSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where uploaddate is less than DEFAULT_UPLOADDATE
        defaultFileinfoShouldNotBeFound("uploaddate.lessThan=" + DEFAULT_UPLOADDATE);

        // Get all the fileinfoList where uploaddate is less than UPDATED_UPLOADDATE
        defaultFileinfoShouldBeFound("uploaddate.lessThan=" + UPDATED_UPLOADDATE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByUploaddateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where uploaddate is greater than DEFAULT_UPLOADDATE
        defaultFileinfoShouldNotBeFound("uploaddate.greaterThan=" + DEFAULT_UPLOADDATE);

        // Get all the fileinfoList where uploaddate is greater than SMALLER_UPLOADDATE
        defaultFileinfoShouldBeFound("uploaddate.greaterThan=" + SMALLER_UPLOADDATE);
    }


    @Test
    @Transactional
    public void getAllFileinfosByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where notes equals to DEFAULT_NOTES
        defaultFileinfoShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the fileinfoList where notes equals to UPDATED_NOTES
        defaultFileinfoShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void getAllFileinfosByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where notes not equals to DEFAULT_NOTES
        defaultFileinfoShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the fileinfoList where notes not equals to UPDATED_NOTES
        defaultFileinfoShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void getAllFileinfosByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultFileinfoShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the fileinfoList where notes equals to UPDATED_NOTES
        defaultFileinfoShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void getAllFileinfosByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where notes is not null
        defaultFileinfoShouldBeFound("notes.specified=true");

        // Get all the fileinfoList where notes is null
        defaultFileinfoShouldNotBeFound("notes.specified=false");
    }
                @Test
    @Transactional
    public void getAllFileinfosByNotesContainsSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where notes contains DEFAULT_NOTES
        defaultFileinfoShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the fileinfoList where notes contains UPDATED_NOTES
        defaultFileinfoShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void getAllFileinfosByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where notes does not contain DEFAULT_NOTES
        defaultFileinfoShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the fileinfoList where notes does not contain UPDATED_NOTES
        defaultFileinfoShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }


    @Test
    @Transactional
    public void getAllFileinfosByFiletypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where filetype equals to DEFAULT_FILETYPE
        defaultFileinfoShouldBeFound("filetype.equals=" + DEFAULT_FILETYPE);

        // Get all the fileinfoList where filetype equals to UPDATED_FILETYPE
        defaultFileinfoShouldNotBeFound("filetype.equals=" + UPDATED_FILETYPE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByFiletypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where filetype not equals to DEFAULT_FILETYPE
        defaultFileinfoShouldNotBeFound("filetype.notEquals=" + DEFAULT_FILETYPE);

        // Get all the fileinfoList where filetype not equals to UPDATED_FILETYPE
        defaultFileinfoShouldBeFound("filetype.notEquals=" + UPDATED_FILETYPE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByFiletypeIsInShouldWork() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where filetype in DEFAULT_FILETYPE or UPDATED_FILETYPE
        defaultFileinfoShouldBeFound("filetype.in=" + DEFAULT_FILETYPE + "," + UPDATED_FILETYPE);

        // Get all the fileinfoList where filetype equals to UPDATED_FILETYPE
        defaultFileinfoShouldNotBeFound("filetype.in=" + UPDATED_FILETYPE);
    }

    @Test
    @Transactional
    public void getAllFileinfosByFiletypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        // Get all the fileinfoList where filetype is not null
        defaultFileinfoShouldBeFound("filetype.specified=true");

        // Get all the fileinfoList where filetype is null
        defaultFileinfoShouldNotBeFound("filetype.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileinfoShouldBeFound(String filter) throws Exception {
        restFileinfoMockMvc.perform(get("/api/fileinfos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileinfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].uploaddate").value(hasItem(sameInstant(DEFAULT_UPLOADDATE))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].filetype").value(hasItem(DEFAULT_FILETYPE.toString())));

        // Check, that the count call also returns 1
        restFileinfoMockMvc.perform(get("/api/fileinfos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFileinfoShouldNotBeFound(String filter) throws Exception {
        restFileinfoMockMvc.perform(get("/api/fileinfos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileinfoMockMvc.perform(get("/api/fileinfos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFileinfo() throws Exception {
        // Get the fileinfo
        restFileinfoMockMvc.perform(get("/api/fileinfos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileinfo() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        int databaseSizeBeforeUpdate = fileinfoRepository.findAll().size();

        // Update the fileinfo
        Fileinfo updatedFileinfo = fileinfoRepository.findById(fileinfo.getId()).get();
        // Disconnect from session so that the updates on updatedFileinfo are not directly saved in db
        em.detach(updatedFileinfo);
        updatedFileinfo
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .uploaddate(UPDATED_UPLOADDATE)
            .notes(UPDATED_NOTES)
            .filetype(UPDATED_FILETYPE);
        FileinfoDTO fileinfoDTO = fileinfoMapper.toDto(updatedFileinfo);

        restFileinfoMockMvc.perform(put("/api/fileinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileinfoDTO)))
            .andExpect(status().isOk());

        // Validate the Fileinfo in the database
        List<Fileinfo> fileinfoList = fileinfoRepository.findAll();
        assertThat(fileinfoList).hasSize(databaseSizeBeforeUpdate);
        Fileinfo testFileinfo = fileinfoList.get(fileinfoList.size() - 1);
        assertThat(testFileinfo.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testFileinfo.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testFileinfo.getUploaddate()).isEqualTo(UPDATED_UPLOADDATE);
        assertThat(testFileinfo.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testFileinfo.getFiletype()).isEqualTo(UPDATED_FILETYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFileinfo() throws Exception {
        int databaseSizeBeforeUpdate = fileinfoRepository.findAll().size();

        // Create the Fileinfo
        FileinfoDTO fileinfoDTO = fileinfoMapper.toDto(fileinfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileinfoMockMvc.perform(put("/api/fileinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileinfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fileinfo in the database
        List<Fileinfo> fileinfoList = fileinfoRepository.findAll();
        assertThat(fileinfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFileinfo() throws Exception {
        // Initialize the database
        fileinfoRepository.saveAndFlush(fileinfo);

        int databaseSizeBeforeDelete = fileinfoRepository.findAll().size();

        // Delete the fileinfo
        restFileinfoMockMvc.perform(delete("/api/fileinfos/{id}", fileinfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fileinfo> fileinfoList = fileinfoRepository.findAll();
        assertThat(fileinfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
