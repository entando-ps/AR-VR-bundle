package it.iedx.login.domain;

import it.iedx.login.service.dto.Asset;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "experience")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Experience {

    private static final String SRC_KEY = "src";

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String path; // PERCORSO DI UPLOAD ASSEGNATO
    private ExperienceStatus status;
    private MultiProfile profiles;
    private ZonedDateTime upload;
    private String xml;
    private String thumbnail;
    @Transient
    private MultipartFile multipartXmlFile; // creation only
    @Transient
    private MultipartFile multipartThumbnailFile; // creation only
    @Transient
    private List<Asset> assets;


    public Experience() {
        this.setStatus(ExperienceStatus.DRAFT);
    }

    public Experience(MultipartFile file, MultipartFile thumbnail, String name, String description) {
        this.name = name;
        this.description = description;
        this.multipartXmlFile = file;
        this.multipartThumbnailFile = thumbnail;
/*
        MultiProfile profilez = new MultiProfile();
        Profile p1 = new Profile();
        Profile p2 = new Profile();

        p1.setAge(Age.YOUNG_ADULT);
        p1.setGender(Gender.FEMALE);
        p1.setSentiment(Sentiment.HAPPY);
        p1.setRace(Race.WHITE);
        p2.setAge(Age.ADULT);
        p2.setGender(Gender.MALE);
        p2.setSentiment(Sentiment.NEUTRAL);
        p2.setRace(Race.WHITE);

        profilez.setProfiles(Arrays.asList(p1, p2));
        this.setProfiles(profilez);
 */
    }

    /**
     * Check if the filename is expected in this scenario
     * @param filename the filename to check against the experience script
     * @return true if the file is expected, false otherwise
     */
    public boolean isExpectedAsset(String filename) {
        if (assets != null && !assets.isEmpty()) {
            Optional<Asset> result = assets
                .stream()
       //         .peek(a -> System.out.println(">>> " + a))
                .filter(a -> StringUtils.isNotBlank(a.getAssetFilename()) && a.getAssetFilename().equals(filename)
                    || (StringUtils.isNotBlank(a.getThumbnailFilename()) && a.getThumbnailFilename().equals(filename)))
                .findAny();
            return result.isPresent();
        }
        return false;
    }

    public boolean isExpectedAsset(String filename, AssetType type) {
        if (assets != null && !assets.isEmpty()) {
            Optional<Asset> result = assets
                .stream()
                .filter(a -> a.getAssetFilename().equals(filename) && a.getType() == type)
                .findAny();
            return result.isPresent();
        }
        return false;
    }


    public Asset findAssetById(String id) {
        Asset asset = null;

        if (StringUtils.isNotBlank(id) && assets != null) {
            List<Asset> result = assets.stream()
                .filter(a -> a.getId().equals(id))
                .collect(Collectors.toList());
            if (!result.isEmpty()) {
                asset = result.get(0);
            }
        }
        return asset;
    }

    public Asset findAssetByIdAndType(final String id, final AssetType type) {
        Asset asset = null;

        if (type == null) {
            return null;
        }
        if (assets != null && StringUtils.isNotBlank(id)) {
            List<Asset> result = assets.stream()
                .filter(a -> a.getId().equals(id) && a.getType() == type)
                .collect(Collectors.toList());
            if (!result.isEmpty()) {
                asset = result.get(0);
            }
        }
        return asset;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ZonedDateTime getUpload() {
        return upload;
    }

    public void setUpload(ZonedDateTime upload) {
        this.upload = upload;
    }

    public MultiProfile getProfiles() {
        return profiles;
    }

    public void setProfiles(MultiProfile profiles) {
        this.profiles = profiles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String note) {
        this.xml = note;
    }

    public ExperienceStatus getStatus() {
        return status;
    }

    public void setStatus(ExperienceStatus status) {
        this.status = status;
    }

    public MultipartFile getMultipartXmlFile() {
        return multipartXmlFile;
    }

    public void setMultipartXmlFile(MultipartFile multipartXmlFile) {
        this.multipartXmlFile = multipartXmlFile;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public MultipartFile getMultipartThumbnailFile() {
        return multipartThumbnailFile;
    }

    public void setMultipartThumbnailFile(MultipartFile multipartThumbnailFile) {
        this.multipartThumbnailFile = multipartThumbnailFile;
    }

    @Override
    public String toString() {
        return "[ " + id + ", " + name + " " + description + " ]";
    }
}

