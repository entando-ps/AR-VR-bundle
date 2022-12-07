package it.iedx.login.service;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.Profile;
import it.iedx.login.service.dto.LoginResponseDTO;

public interface PairingService {

    /**
     * Create an access code setting all important field and status
     * (PENDING if a device is available, EXTERNAL otherwise)
     *
     * @param profile@return the newly generated access code
     */
    AccessCode create(Profile profile);

    /**
     * Check if there's a user waiting to use the given device
     * @param uid the unique ID of the device
     * @return the list of available experiences as well as the consumed access code
     */
    LoginResponseDTO consumeByDeviceId(String uid);

    /**
     * Consume the desired access code and return the experiences available for the pending user
     * @param code the access code to consume
     * @return the list of available experiences as well as the consumed access code
     */
    LoginResponseDTO consumeByAccessCode(String code);
}
