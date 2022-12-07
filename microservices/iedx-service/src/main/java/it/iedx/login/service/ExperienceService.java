package it.iedx.login.service;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.Age;
import it.iedx.login.domain.AssetType;
import it.iedx.login.domain.Experience;
import it.iedx.login.domain.ExperienceStatus;
import it.iedx.login.service.dto.Asset;
import it.iedx.login.service.dto.ExperienceDTO;
import it.iedx.login.service.dto.ExperienceSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ExperienceService {

    Experience parseScenario(String xml);

    void parse(Experience experience);

    Experience save(Experience experience);

    /**
     * Delete the "id" experience and all the file associated
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the experiences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Experience> findAll(Pageable pageable);


    /**
     * Load and parse the desired experience
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Experience> load(Long id);

    Optional<Experience> get(Long id);

    /**
     * Associate a new asset to the experience. Asset with the same name will be overwritten
     * @param file the file associated to the asset to save
     * @param thumb the thumbnail associated
     * @param id the unique id of the experience whose asset belongs to
     * @param isAsset false in case of XML or experience thumbnail update, true otherwise
     * @return the updated list of assets, null otherwise
     */
    List<Asset> saveAsset(MultipartFile file, MultipartFile thumb, Long id, boolean isAsset);

    /**
     * Return the file of the desired asset
     * @param id unique id of the experience
     * @param assetId unique identifier of the asset
     * @param type optional type of the desired asset
     * @return the File containing the desired asset
     * @throws IOException in case of IO error
     */
    File getAssetFile(Long id, String assetId, AssetType type) throws IOException;

    /**
     * Return the thumbnail file of the desired asset
     * @param id unique id of the experience
     * @param assetId unique identifier of the asset
     * @param type optional type of the desired asset
     * @return the thumbnail File of the desired asset
     * @throws IOException in case of IO error
     */
    File getAssetThumbnail(Long id, String assetId, AssetType type) throws IOException;

    /**
     * Return the XML of the given experience
     * @param id unique id of the experience
     * @return the experience XML of the desired experience
     */
    File getExperienceXml(Long id);

    /**
     * Return the thumbnail of the given experience
     * @param id unique id of the experience
     * @return the thumbnail of the given experience
     */
    File getExperienceThumbnail(Long id);

    /**
     * Return the details of an experience
     * @param id unique id of the experience
     * @return the list of the assets
     */
    Optional<ExperienceDTO> describe(Long id);

    /**
     * Find experience by age
     * @param age the age to look experiences for
     * @return the desired experiences, if any
     */
    List<ExperienceSummaryDTO> findExperienceByAge(Age age);

    /**
     * Find experience by age
     * @param accessCode code associated to a profile
     * @return the desired experiences, if any
     */
    List<ExperienceSummaryDTO> findExperienceByAge(AccessCode accessCode);

    Boolean changeExperienceStatus(Long id, ExperienceStatus status);

    /**
     * Get all experiences (not paginated)
     * @return the list of all experiences
     */
    List<ExperienceSummaryDTO> getExperiences();

    /**
     * Get all experiences matching the given status (not paginated)
     * @param status the status to look experiences for
     * @return the list of experiences with the desired status
     */
    List<ExperienceSummaryDTO> getExperienceByStatus(Integer status);
}
