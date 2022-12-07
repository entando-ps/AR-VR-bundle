package it.iedx.login.repository;

import it.iedx.login.domain.Experience;
import it.iedx.login.domain.ExperienceStatus;
import it.iedx.login.service.dto.ExperienceSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long>, JpaSpecificationExecutor<Experience> {

    List<Experience> statusIs(ExperienceStatus status);

    Optional<Experience> nameIs(String name);

    @Query(
        value = "SELECT id, name, description FROM ( SELECT id, JSON_EXTRACT(profiles, '$.profiles[*].age') AS ages, name, description FROM experience WHERE profiles IS NOT NULL AND status = '1') AS temp_table WHERE JSON_CONTAINS(ages, json_quote(?1), '$') = 1  OR JSON_CONTAINS(ages, json_quote(?2), '$') = 1",
        nativeQuery = true
    )
    List<ExperienceSummaryDTO> profilesContainsAge(String first, String second);

    @Query(
        value = "SELECT id, name, description FROM ( SELECT id, JSON_EXTRACT(profiles, '$.profiles[*].age') AS ages, name, description FROM experience WHERE profiles IS NOT NULL AND status = '1') AS temp_table WHERE JSON_CONTAINS(ages, json_quote(?1), '$') = 1",
        nativeQuery = true
    )
    List<ExperienceSummaryDTO> profilesContainsAge(String age);

    @Query(
        value = "SELECT id, name, description FROM experience",
        nativeQuery = true
    )
    List<ExperienceSummaryDTO> getExperiencesSummary();

    @Query(
        value = "SELECT id, name, description FROM experience WHERE status = ?1",
        nativeQuery = true
    )
    List<ExperienceSummaryDTO> findExperienceByStatusSummary(Integer status);

}
