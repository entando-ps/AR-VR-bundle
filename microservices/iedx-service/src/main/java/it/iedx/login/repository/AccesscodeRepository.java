package it.iedx.login.repository;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.LeaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Spring Data  repository for the AccessCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccesscodeRepository extends JpaRepository<AccessCode, Long>, JpaSpecificationExecutor<AccessCode> {

    @Query(
        value = "SELECT * FROM accesscode WHERE code = ?1 AND (status = ?2 OR status = ?3)",
        nativeQuery = true
    )
    Optional<AccessCode> getAccessCodeByCodeAndStatus(String accessCode, LeaseStatus status1, LeaseStatus status2);

    Optional<AccessCode> deviceIdIsAndStatusIs(Long id, LeaseStatus status1);

    @Modifying
    @Query(value = "update AccessCode a set a.status = :status where a.id = :id")
    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") LeaseStatus status);

    @Modifying
    @Query(value = "update AccessCode a set a.checked = :time where a.id = :id")
    void updateChecked(@Param(value = "id") Long id, @Param(value = "time") ZonedDateTime time);

    @Modifying
    @Query(value = "update AccessCode a set a.status = :status where a.checked < :time AND a.deviceId IS NOT NULL AND a.status = 1") // IN USE
    void markExpired(@Param(value = "status") LeaseStatus status, @Param(value = "time") ZonedDateTime time);

    @Modifying
    @Query(value = "update AccessCode a set a.status = :status, a.checked = :time where a.generated < :time AND (a.status = 0 OR a.status = 3)") // PENDING, EXTERNAL
    void markUnused(@Param(value = "status") LeaseStatus status, @Param(value = "time") ZonedDateTime time);
}
