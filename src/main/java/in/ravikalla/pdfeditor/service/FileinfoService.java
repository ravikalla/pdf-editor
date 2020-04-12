package in.ravikalla.pdfeditor.service;

import in.ravikalla.pdfeditor.domain.Fileinfo;
import in.ravikalla.pdfeditor.repository.FileinfoRepository;
import in.ravikalla.pdfeditor.service.dto.FileinfoDTO;
import in.ravikalla.pdfeditor.service.mapper.FileinfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Fileinfo}.
 */
@Service
@Transactional
public class FileinfoService {

    private final Logger log = LoggerFactory.getLogger(FileinfoService.class);

    private final FileinfoRepository fileinfoRepository;

    private final FileinfoMapper fileinfoMapper;

    public FileinfoService(FileinfoRepository fileinfoRepository, FileinfoMapper fileinfoMapper) {
        this.fileinfoRepository = fileinfoRepository;
        this.fileinfoMapper = fileinfoMapper;
    }

    /**
     * Save a fileinfo.
     *
     * @param fileinfoDTO the entity to save.
     * @return the persisted entity.
     */
    public FileinfoDTO save(FileinfoDTO fileinfoDTO) {
        log.debug("Request to save Fileinfo : {}", fileinfoDTO);
        Fileinfo fileinfo = fileinfoMapper.toEntity(fileinfoDTO);
        fileinfo = fileinfoRepository.save(fileinfo);
        return fileinfoMapper.toDto(fileinfo);
    }

    /**
     * Get all the fileinfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FileinfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fileinfos");
        return fileinfoRepository.findAll(pageable)
            .map(fileinfoMapper::toDto);
    }


    /**
     * Get one fileinfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FileinfoDTO> findOne(Long id) {
        log.debug("Request to get Fileinfo : {}", id);
        return fileinfoRepository.findById(id)
            .map(fileinfoMapper::toDto);
    }

    /**
     * Delete the fileinfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Fileinfo : {}", id);
        fileinfoRepository.deleteById(id);
    }
}
