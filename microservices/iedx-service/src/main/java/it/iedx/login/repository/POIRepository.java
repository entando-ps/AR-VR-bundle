package it.iedx.login.repository;

import it.iedx.login.domain.POI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface POIRepository extends JpaRepository<POI, Long>, JpaSpecificationExecutor<POI> {

}
