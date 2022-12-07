package it.iedx.login.service.mapper;

import it.iedx.login.domain.*;
import it.iedx.login.service.dto.ConferenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conference} and its DTO {@link ConferenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConferenceMapper extends EntityMapper<ConferenceDTO, Conference> {}
