package it.iedx.login.service;

import it.iedx.login.domain.POI;

import java.io.File;
import java.util.List;

public interface POIService {

    /**
     * Save a new POI
     * @param poi the object to persist
     * @return the saved object
     */
    POI save(POI poi);

    File getPOIImage(Long filename);

    /**
     * @return the list of POI
     */
    List<POI> findAll();

    /**
     * Delete the desired POI
     * @param id id of  the desired POI
     */
    void delete(Long id);
}
