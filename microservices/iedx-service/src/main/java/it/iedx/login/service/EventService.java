package it.iedx.login.service;

import it.iedx.login.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Event save(Event event);

    Page<Event> findAll(Pageable pageable);

    List<Event> findAll();

    Optional<Event> findOne(Long id);

    /**
     * Delete the "id" Event.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
