package in.ravikalla.pdfeditor.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import in.ravikalla.pdfeditor.domain.Fileinfo;
import in.ravikalla.pdfeditor.domain.*; // for static metamodels
import in.ravikalla.pdfeditor.repository.FileinfoRepository;
import in.ravikalla.pdfeditor.service.dto.FileinfoCriteria;
import in.ravikalla.pdfeditor.service.dto.FileinfoDTO;
import in.ravikalla.pdfeditor.service.mapper.FileinfoMapper;

/**
 * Service for executing complex queries for {@link Fileinfo} entities in the database.
 * The main input is a {@link FileinfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileinfoDTO} or a {@link Page} of {@link FileinfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileinfoQueryService extends QueryService<Fileinfo> {

    private final Logger log = LoggerFactory.getLogger(FileinfoQueryService.class);

    private final FileinfoRepository fileinfoRepository;

    private final FileinfoMapper fileinfoMapper;

    public FileinfoQueryService(FileinfoRepository fileinfoRepository, FileinfoMapper fileinfoMapper) {
        this.fileinfoRepository = fileinfoRepository;
        this.fileinfoMapper = fileinfoMapper;
    }

    /**
     * Return a {@link List} of {@link FileinfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileinfoDTO> findByCriteria(FileinfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fileinfo> specification = createSpecification(criteria);
        return fileinfoMapper.toDto(fileinfoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FileinfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileinfoDTO> findByCriteria(FileinfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fileinfo> specification = createSpecification(criteria);
        return fileinfoRepository.findAll(specification, page)
            .map(fileinfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileinfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fileinfo> specification = createSpecification(criteria);
        return fileinfoRepository.count(specification);
    }

    /**
     * Function to convert {@link FileinfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Fileinfo> createSpecification(FileinfoCriteria criteria) {
        Specification<Fileinfo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Fileinfo_.id));
            }
            if (criteria.getUploaddate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploaddate(), Fileinfo_.uploaddate));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Fileinfo_.notes));
            }
            if (criteria.getFiletype() != null) {
                specification = specification.and(buildSpecification(criteria.getFiletype(), Fileinfo_.filetype));
            }
        }
        return specification;
    }
}
