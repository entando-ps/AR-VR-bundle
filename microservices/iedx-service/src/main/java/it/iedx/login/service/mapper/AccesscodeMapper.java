package it.iedx.login.service.mapper;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.service.dto.AccessCodeDTO;
import org.mapstruct.Mapper;


/**
 * Mapper for the entity {@link AccessCode} and its DTO {@link AccessCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccesscodeMapper extends EntityMapper<AccessCodeDTO, AccessCode> {

    default AccessCode fromId(Long id) {
        if (id == null) {
            return null;
        }
        AccessCode accesscode = new AccessCode();
        accesscode.setId(id);
        return accesscode;
    }
}
