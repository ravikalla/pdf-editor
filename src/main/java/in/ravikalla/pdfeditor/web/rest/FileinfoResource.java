package in.ravikalla.pdfeditor.web.rest;

import in.ravikalla.pdfeditor.service.FileinfoService;
import in.ravikalla.pdfeditor.web.rest.errors.BadRequestAlertException;
import in.ravikalla.pdfeditor.service.dto.FileinfoDTO;
import in.ravikalla.pdfeditor.service.dto.FileinfoCriteria;
import in.ravikalla.pdfeditor.service.FileinfoQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link in.ravikalla.pdfeditor.domain.Fileinfo}.
 */
@RestController
@RequestMapping("/api")
public class FileinfoResource {

    private final Logger log = LoggerFactory.getLogger(FileinfoResource.class);

    private static final String ENTITY_NAME = "fileinfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileinfoService fileinfoService;

    private final FileinfoQueryService fileinfoQueryService;

    public FileinfoResource(FileinfoService fileinfoService, FileinfoQueryService fileinfoQueryService) {
        this.fileinfoService = fileinfoService;
        this.fileinfoQueryService = fileinfoQueryService;
    }

    /**
     * {@code POST  /fileinfos} : Create a new fileinfo.
     *
     * @param fileinfoDTO the fileinfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileinfoDTO, or with status {@code 400 (Bad Request)} if the fileinfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fileinfos")
    public ResponseEntity<FileinfoDTO> createFileinfo(@Valid @RequestBody FileinfoDTO fileinfoDTO) throws URISyntaxException {
        log.debug("REST request to save Fileinfo : {}", fileinfoDTO);
        if (fileinfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileinfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileinfoDTO result = fileinfoService.save(fileinfoDTO);
        return ResponseEntity.created(new URI("/api/fileinfos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fileinfos} : Updates an existing fileinfo.
     *
     * @param fileinfoDTO the fileinfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileinfoDTO,
     * or with status {@code 400 (Bad Request)} if the fileinfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileinfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fileinfos")
    public ResponseEntity<FileinfoDTO> updateFileinfo(@Valid @RequestBody FileinfoDTO fileinfoDTO) throws URISyntaxException {
        log.debug("REST request to update Fileinfo : {}", fileinfoDTO);
        if (fileinfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileinfoDTO result = fileinfoService.save(fileinfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fileinfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fileinfos} : get all the fileinfos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileinfos in body.
     */
    @GetMapping("/fileinfos")
    public ResponseEntity<List<FileinfoDTO>> getAllFileinfos(FileinfoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Fileinfos by criteria: {}", criteria);
        Page<FileinfoDTO> page = fileinfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /fileinfos/count} : count all the fileinfos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/fileinfos/count")
    public ResponseEntity<Long> countFileinfos(FileinfoCriteria criteria) {
        log.debug("REST request to count Fileinfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(fileinfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fileinfos/:id} : get the "id" fileinfo.
     *
     * @param id the id of the fileinfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileinfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fileinfos/{id}")
    public ResponseEntity<FileinfoDTO> getFileinfo(@PathVariable Long id) {
        log.debug("REST request to get Fileinfo : {}", id);
        Optional<FileinfoDTO> fileinfoDTO = fileinfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileinfoDTO);
    }

    /**
     * {@code DELETE  /fileinfos/:id} : delete the "id" fileinfo.
     *
     * @param id the id of the fileinfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fileinfos/{id}")
    public ResponseEntity<Void> deleteFileinfo(@PathVariable Long id) {
        log.debug("REST request to delete Fileinfo : {}", id);
        fileinfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
