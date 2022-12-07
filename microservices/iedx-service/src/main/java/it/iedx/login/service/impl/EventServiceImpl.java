package it.iedx.login.service.impl;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.Event;
import it.iedx.login.domain.EventType;
import it.iedx.login.domain.Experience;
import it.iedx.login.repository.AccesscodeRepository;
import it.iedx.login.repository.EventRepository;
import it.iedx.login.repository.ExperienceRepository;
import it.iedx.login.service.EventService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final ExperienceRepository experienceRepository;
    private final AccesscodeRepository accesscodeRepository;

    public EventServiceImpl(EventRepository eventRepository, ExperienceRepository experienceRepository, AccesscodeRepository accesscodeRepository) {
        this.eventRepository = eventRepository;
        this.experienceRepository = experienceRepository;
        this.accesscodeRepository = accesscodeRepository;
    }

    @Override
    public Event save(Event event) {
        log.debug("Request to save a new event");
        boolean proceed = true;

        if (event != null ) {
            final ZonedDateTime now = ZonedDateTime.now();

            if (event.getExperienceId() == null) {
                log.error("Cannot save Event {} because the experienceId is not specified", event);
                proceed = false;
            } else {
                Optional<Experience> experienceOpt = experienceRepository.findById(event.getExperienceId());
                if (!experienceOpt.isPresent()
//                    || experienceOpt.get().getStatus() != ExperienceStatus.PUBLISHED
                ) {
                    log.debug("Cannot save Event {} because the experienceId {} is unknown ", event, event.getExperienceId());
                    proceed = false;
                }
            }

            if (event.getAccessCodeId() == null) {
                log.error("Cannot save Event {} because the accessCodeId is not specified", event);
                proceed = false;
            } else {
                Optional<AccessCode> accessCode = accesscodeRepository.findById(event.getAccessCodeId());
                if (!accessCode.isPresent()) {
                    log.debug("Cannot save Event {} because the accessCodeId {} is unknown ", event, event.getAccessCodeId());
                    proceed = false;
                }
            }

            if (event.getType() == null) {
                log.error("Cannot save Event {} because the type is not specified", event);
                proceed = false;
            }

            // validazione
//            if (event.getType() == EventType.PAUSE ||
//                event.getType() == EventType.RESUME ||
//                event.getType() == EventType.SKIP_FORWARD ||
//                event.getType() == EventType.SKIP_BACKWARD ) {
//                // do nothing, the date will suffice
//            }

            if (StringUtils.isBlank(event.getScenarioElementId()) && (
                event.getType() == EventType.HOTSPOT_SELECTION
                    || event.getType() == EventType.OPTIONSPOPUP_CHOICE
                    || event.getType() == EventType.DETAILSPANEL_EXPAND
                    || event.getType() == EventType.DETAILSPANEL_MINIMIZE
                )) {
                proceed = false;
                log.warn("ScenarioElementID is expected for event type {}", event.getType().getCode());
            }

            if (StringUtils.isBlank(event.getChoiceId()) && (
                event.getType() == EventType.EXPERIENCE_START
                    || event.getType() == EventType.SCENARIO_SELECTION
                    || event.getType() == EventType.OPTIONSPOPUP_CHOICE
            )) {
                proceed = false;
                log.warn("ChoiceID is expected for event type {}", event.getType().getCode());
            }

            if (proceed) {
                // always set the date
                event.setDate(now);
                eventRepository.save(event);
            } else {
                event = null;
            }
        }
        return event;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        log.debug("Request to get all evidence records");
        return eventRepository.findAll(pageable);
    }

    @Override
    public List<Event> findAll() {
        log.debug("Request to get all evidences");
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> findOne(Long id) {
        log.debug("Request to get evidence : {}", id);
        return eventRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete evidence : {}", id);
        eventRepository.deleteById(id);
    }
}
