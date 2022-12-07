package it.iedx.login.service.impl;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.LeaseStatus;
import it.iedx.login.domain.Profile;
import it.iedx.login.repository.AccesscodeRepository;
import it.iedx.login.service.AccesscodeService;
import it.iedx.login.service.mapper.AccesscodeMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AccessCode}.
 */
@Service
@Transactional
public class AccesscodeServiceImpl implements AccesscodeService {

    private static final long WATCHDOG_INUSE_SECONDS = 180;
    private static final long WATCHDOG_PENDING_SECONDS = 300;

    private final Logger log = LoggerFactory.getLogger(AccesscodeServiceImpl.class);

    private final AccesscodeRepository accesscodeRepository;

    private final AccesscodeMapper accesscodeMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public AccesscodeServiceImpl(AccesscodeRepository accesscodeRepository, AccesscodeMapper accesscodeMapper) {
        this.accesscodeRepository = accesscodeRepository;
        this.accesscodeMapper = accesscodeMapper;
    }

    @Override
    public AccessCode save(AccessCode accesscode) {
        log.debug("Request to save AccessCode : {}", accesscode);
        if (null == accesscode.getId()) {
            // new
            accesscode.setGenerated(ZonedDateTime.now());
            accesscode.setCode(generateRandomString(CODE_LENGTH));
            log.debug("Saving NEW access code with value {}", accesscode.getCode());
        } else {
            // upadate
            accesscode.setChecked(ZonedDateTime.now());
            log.debug("Updating existing access code {} ", accesscode.getCode());
        }
        return accesscodeRepository.save(accesscode);
    }

    @Override
    public LeaseStatus keepAlive(long id) {
        LeaseStatus status = null;

        log.debug("Request to keep alive AccessCode : {}", id);
        Optional<AccessCode> opt = accesscodeRepository.findById(id);
        if (opt.isPresent()) {
            status = opt.get().getStatus();
            accesscodeRepository.updateChecked(id, ZonedDateTime.now());
            log.debug("last check updated");
        }
        return status;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccessCode> findAll(Pageable pageable) {
        log.debug("Request to get all Accesscodes");
        return accesscodeRepository.findAll(pageable);
//            .map(accesscodeMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AccessCode> findOne(Long id) {
        log.debug("Request to get AccessCode : {}", id);
        return accesscodeRepository.findById(id);
    }


    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccessCode : {}", id);
        accesscodeRepository.deleteById(id);
    }

    @Override
    public Profile getProfileByAccessCode(String accessCode) {
        Optional<AccessCode> ac = accesscodeRepository.getAccessCodeByCodeAndStatus(accessCode, LeaseStatus.PENDING, LeaseStatus.EXTERNAL);
        return ac.map(AccessCode::getProfile).orElse(null);
    }

    @Override
    public Optional<AccessCode> loadPendingByAccessCode(String accessCode) {
        Optional<AccessCode> opt = Optional.empty();

        if (StringUtils.isNotBlank(accessCode)) {
            opt = accesscodeRepository.getAccessCodeByCodeAndStatus(accessCode, LeaseStatus.PENDING, LeaseStatus.EXTERNAL);
        }
        return opt;
    }

    @Override
    public Optional<AccessCode> findPendingByDevice(Long id) {
        Optional<AccessCode> opt = Optional.empty();

        if (null != id) {
            opt = accesscodeRepository.deviceIdIsAndStatusIs(id, LeaseStatus.PENDING);
        }
        return opt;
    }

    @Override
    public LeaseStatus updateStatus(Long id, LeaseStatus status) {
        accesscodeRepository.updateStatus(id, status);
        return status;
    }

    @Scheduled(initialDelay = 5000, fixedRate = 30 * 1000)
    public void markExpiredExperiences() {
        ZonedDateTime target = ZonedDateTime.now();
        target = target.minusSeconds(WATCHDOG_INUSE_SECONDS);
        log.debug("Voiding IN USE  access codes older than {} seconds (before {})", WATCHDOG_INUSE_SECONDS, target);
        accesscodeRepository.markExpired(LeaseStatus.RELEASED, target);
    }

    @Scheduled(initialDelay = 10000, fixedRate = 60 * 1000)
    public void markUnusedExperiences() {
        ZonedDateTime target = ZonedDateTime.now();
        target = target.minusSeconds(WATCHDOG_PENDING_SECONDS);
        log.debug("Voiding PENDING access codes not used in {} seconds (before {})", WATCHDOG_PENDING_SECONDS, target);
        accesscodeRepository.markUnused(LeaseStatus.RELEASED, target);
    }

    protected String generateRandomString(int length) {
        String[] consonants = {"B", "C", "D", "F", "G", "H",
            "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T",
            "V", "W", "X", "Y", "Z"};
        String[] vowels = {"A", "E", "I", "O", "U"};
        StringBuilder pwd = new StringBuilder();

        int randomConsonantIdx;
        int randomVowelIdx;

        for (int i = 1; i < length + 1; i++) {
            if ((i % 2) == 0) {
                // vocale
                randomVowelIdx = (int) ((Math.random() * (V_MAX - MIN)) + MIN);
                pwd.append(vowels[randomVowelIdx]);
            } else {
                // consonante
                randomConsonantIdx = (int) ((Math.random() * (C_MAX - MIN)) + MIN);
                pwd.append(consonants[randomConsonantIdx]);
            }
        }
        return pwd.toString();
    }

    public final static int MIN = 0;
    public final static int C_MAX = 20;
    public final static int V_MAX = 4;

    public static final int CODE_LENGTH = 4;
}
