package it.iedx.login.service.impl;

import it.iedx.login.domain.Conference;
import it.iedx.login.repository.ConferenceRepository;
import it.iedx.login.service.ConferenceService;
import it.iedx.login.service.dto.ConferenceDTO;
import it.iedx.login.service.mapper.ConferenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conference}.
 */
@Service
@Transactional
public class ConferenceServiceImpl implements ConferenceService {

    private final Logger log = LoggerFactory.getLogger(ConferenceServiceImpl.class);

    private final ConferenceRepository conferenceRepository;

    private final ConferenceMapper conferenceMapper;

    public ConferenceServiceImpl(ConferenceRepository conferenceRepository, ConferenceMapper conferenceMapper) {
        this.conferenceRepository = conferenceRepository;
        this.conferenceMapper = conferenceMapper;
    }

    @Override
    public ConferenceDTO save(ConferenceDTO conferenceDTO) {
        log.debug("Request to save Conference : {}", conferenceDTO);
        Conference conference = conferenceMapper.toEntity(conferenceDTO);
        conference = conferenceRepository.save(conference);
        return conferenceMapper.toDto(conference);
    }

    @Override
    public Optional<ConferenceDTO> partialUpdate(ConferenceDTO conferenceDTO) {
        log.debug("Request to partially update Conference : {}", conferenceDTO);

        return conferenceRepository
            .findById(conferenceDTO.getId())
            .map(existingConference -> {
                conferenceMapper.partialUpdate(existingConference, conferenceDTO);

                return existingConference;
            })
            .map(conferenceRepository::save)
            .map(conferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConferenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conferences");
        return conferenceRepository.findAll(pageable).map(conferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConferenceDTO> findOne(Long id) {
        log.debug("Request to get Conference : {}", id);
        return conferenceRepository.findById(id).map(conferenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Conference : {}", id);
        conferenceRepository.deleteById(id);
    }
}
