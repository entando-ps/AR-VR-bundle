package it.iedx.login.service;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.repository.AccesscodeRepository;
import it.iedx.login.service.dto.AccessCodeDTO;
import it.iedx.login.service.dto.AccesscodeCriteria;
import it.iedx.login.service.mapper.AccesscodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.util.List;

/**
 * Service for executing complex queries for {@link AccessCode} entities in the database.
 * The main input is a {@link AccesscodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccessCodeDTO} or a {@link Page} of {@link AccessCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccesscodeQueryService extends QueryService<AccessCode> {

    private final Logger log = LoggerFactory.getLogger(AccesscodeQueryService.class);

    private final AccesscodeRepository accesscodeRepository;

    private final AccesscodeMapper accesscodeMapper;

    public AccesscodeQueryService(AccesscodeRepository accesscodeRepository, AccesscodeMapper accesscodeMapper) {
        this.accesscodeRepository = accesscodeRepository;
        this.accesscodeMapper = accesscodeMapper;
    }

    /**
     * Return a {@link List} of {@link AccessCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccessCodeDTO> findByCriteria(AccesscodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccessCode> specification = createSpecification(criteria);
        return accesscodeMapper.toDto(accesscodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AccessCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccessCodeDTO> findByCriteria(AccesscodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccessCode> specification = createSpecification(criteria);
        return accesscodeRepository.findAll(specification, page)
            .map(accesscodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccesscodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccessCode> specification = createSpecification(criteria);
        return accesscodeRepository.count(specification);
    }

    /**
     * Function to convert {@link AccesscodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccessCode> createSpecification(AccesscodeCriteria criteria) {
        Specification<AccessCode> specification = Specification.where(null);
        if (criteria != null) {
//            if (criteria.getId() != null) {
//                specification = specification.and(buildRangeSpecification(criteria.getId(), Accesscode_.id));
//            }
//            if (criteria.getCode() != null) {
//                specification = specification.and(buildStringSpecification(criteria.getCode(), Accesscode_.code));
//            }
//            if (criteria.getGenerated() != null) {
//                specification = specification.and(buildRangeSpecification(criteria.getGenerated(), Accesscode_.generated));
//            }
//            if (criteria.getStatus() != null) {
//                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Accesscode_.status));
//            }
//            if (criteria.getUsed() != null) {
//                specification = specification.and(buildRangeSpecification(criteria.getUsed(), Accesscode_.used));
//            }
//            if (criteria.getQrcode() != null) {
//                specification = specification.and(buildStringSpecification(criteria.getQrcode(), Accesscode_.qrcode));
//            }
        }
        return specification;
    }
}
