package it.iedx.login.service.impl;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.Device;
import it.iedx.login.domain.DeviceStatus;
import it.iedx.login.domain.LeaseStatus;
import it.iedx.login.domain.Profile;
import it.iedx.login.service.AccesscodeService;
import it.iedx.login.service.DeviceService;
import it.iedx.login.service.ExperienceService;
import it.iedx.login.service.PairingService;
import it.iedx.login.service.dto.ExperienceSummaryDTO;
import it.iedx.login.service.dto.LoginResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PairingServiceImpl implements PairingService {

    public  static final String BASE_QR_CODE = "iedx://accesscode?";

    private final Logger log = LoggerFactory.getLogger(PairingServiceImpl.class);

    private final DeviceService deviceService;
    private final AccesscodeService accesscodeService;
    private final ExperienceService experienceService;

    public PairingServiceImpl(DeviceService deviceService, AccesscodeService accesscodeService, ExperienceService experienceService) {
        this.deviceService = deviceService;
        this.accesscodeService = accesscodeService;
        this.experienceService = experienceService;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AccessCode create(Profile profile) {
        AccessCode code = new AccessCode();

        code.setProfile(profile);
        Device device = findDevice();
        if (null != device) {
            log.debug("Creating access code with status PENDING");
            code.setStatus(LeaseStatus.PENDING);
            code.setDeviceId(device.getId());
            code.setMessage(device.getNote());
        } else {
            log.debug("Creating access code with status EXTERNAL");
            code.setStatus(LeaseStatus.EXTERNAL);
        }
        accesscodeService.save(code);
        // create QR code
        code.setQrcode(createQRCode(code.getCode()));
        return code;
    }

    /**
     * Generate QR code payload
     * @param code
     * @return
     */
    private String createQRCode(String code) {
        String qrcode = "";
        if (StringUtils.isNotBlank(code)) {
            qrcode = BASE_QR_CODE.concat(code);
        }
        return qrcode;
    }

    /**
     * Find e device available for pairing with access code
     * @return return the list of available devices
     */

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Device findDevice() {
        Optional<Device> available = deviceService.getAvailableDevices();
        return available.orElse(null);
    }

    @Override
    public LoginResponseDTO consumeByDeviceId(String uid) {
        final LoginResponseDTO response = new LoginResponseDTO();

        Optional<Device> device = deviceService.getDeviceByUID(uid);
        if (device.isPresent() && device.get().getStatus() == DeviceStatus.AVAILABLE) {
            Optional<AccessCode> accessCode = accesscodeService.findPendingByDevice(device.get().getId());
            accessCode.ifPresent(a -> doConsume(a, response));
            if (!accessCode.isPresent()) {
                log.debug("No access code found for device {}", uid);
            }
        } else {
            log.debug("device {} unknown or not available", uid);
        }
        return response;
    }

    @Override
    public LoginResponseDTO consumeByAccessCode(String code) {
        final LoginResponseDTO response = new LoginResponseDTO();

        if (StringUtils.isNotBlank(code)) {
            Optional<AccessCode> optAccessCode = accesscodeService.loadPendingByAccessCode(code);
            optAccessCode.ifPresent(a -> doConsume(a, response));
            if (!optAccessCode.isPresent()) {
                log.debug("No access code found for access code {}", code);
            }
        }
        return response;
    }

    protected void doConsume(AccessCode accessCode, LoginResponseDTO response) {

        if (null != accessCode) {
            response.setAccessCodeId(accessCode.getId());
            log.debug("Consuming access code {} for device {}", accessCode.getCode(), accessCode.getDeviceId());
            // the update will also set dates accordingly
            accessCode.setStatus(LeaseStatus.INUSE);
            // update access code
            accesscodeService.save(accessCode);
            // return the experiences for the given access code
            final List<ExperienceSummaryDTO> experiences = experienceService.findExperienceByAge(accessCode);
            response.setExperiences(experiences);
        }
    }

}
