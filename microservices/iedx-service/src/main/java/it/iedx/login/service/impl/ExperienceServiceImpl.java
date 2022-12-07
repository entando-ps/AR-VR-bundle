package it.iedx.login.service.impl;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.Age;
import it.iedx.login.domain.AssetType;
import it.iedx.login.domain.Experience;
import it.iedx.login.domain.ExperienceStatus;
import it.iedx.login.domain.MultiProfile;
import it.iedx.login.domain.Profile;
import it.iedx.login.repository.ExperienceRepository;
import it.iedx.login.service.ExperienceService;
import it.iedx.login.service.dto.Asset;
import it.iedx.login.service.dto.ExperienceDTO;
import it.iedx.login.service.dto.ExperienceSummaryDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class ExperienceServiceImpl implements ExperienceService {

    private static final String KEY_TITLE = "title";
    private static final String KEY_SRC = "src";
    private static final String KEY_IMAGESRC = "imageSrc";
    private static final String KEY_LABEL = "label";
    private static final String KEY_THUMBNAIL = "thumbnail";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NAME = "experienceName";
    private static final String EXPERIENCE_FILE_ID = "experienceScript";
    private static final String EXPERIENCE_THUMB_FILE_ID = "experienceThumb";

    private final Logger log = LoggerFactory.getLogger(ExperienceServiceImpl.class);

    private final ExperienceRepository experienceRepository;

    private final FileStorageServiceImpl fileStorageService;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, FileStorageServiceImpl fileStorageService) {
        this.experienceRepository = experienceRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Experience parseScenario(String xml) {
        Experience experience = null;
        if (StringUtils.isNotBlank(xml)) {
            InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            experience = parseScenario(is);
        }
        return experience;
    }

    @Override
    public void parse(Experience experience) {
        if (null != experience) {
            Path baseDir = fileStorageService.getUploadDir();
            Path relativePath = Paths.get(experience.getPath());
            Path filename = Paths.get(experience.getXml());

            Path physFilePath = baseDir.resolve(relativePath.resolve(filename));
            if (physFilePath.toFile().exists()) {
                try {
                    InputStream is = new FileInputStream(physFilePath.toFile());
                    parseScenario(experience, is);
                } catch (Throwable t) {
                    log.error("error parsing scenario! ", t);
                }
                // parse the assets file for presence
                experience.getAssets()
                    .forEach(a -> {
                        String curFilenameString = null;

                        if (StringUtils.isNotBlank(a.getAssetFilename())) {
                            curFilenameString = a.getAssetFilename();
                            Path assetPath = Paths.get(curFilenameString);
                            Path check = baseDir.resolve(relativePath.resolve(assetPath));

//                        System.out.println(">A> " + check);
                            a.setAssetPresentOnDisk(check.toFile().exists());
                        }  else {
                            a.setAssetPresentOnDisk(false);
                        }
                        // thumbnail
                        if (StringUtils.isNotBlank(a.getThumbnailFilename())) {
                            curFilenameString = a.getThumbnailFilename();
                            Path thumbPath = Paths.get(curFilenameString);
                            Path check = baseDir.resolve(relativePath.resolve(thumbPath));

//                            System.out.println(">T> " + check + "\n");
                            a.setThumbnailPresentOnDisk(check.toFile().exists());
                        } else {
                            a.setThumbnailPresentOnDisk(false);
                        }
                    });
            } else {
                log.debug("Cannot parse master XML file!");
            }
        }
    }

    protected Experience parseScenario(InputStream is) {
        Experience experience = new Experience();
        return parseScenario(experience, is);
    }

    protected Experience parseScenario(Experience experience, InputStream is) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document experienceDocument = builder.parse(is);
            // metadata
            extractMetadata(experience, experienceDocument);
            // get video scenarios...
            List<Asset> assets = getVideoScenario(experienceDocument);
            // get image scenarios...
            List<Asset> images = getImageScenario(experienceDocument);
            assets.addAll(images);
            // get the hotspots
            List<Asset> hotspots = getHotspot(experienceDocument);
            assets.addAll(hotspots);
            // get subtitles
            List<Asset> subtitles = getSubtitle(experienceDocument);
            assets.addAll(subtitles);
            // get
            List<Asset> panels = getDetailsPanel(experienceDocument);
            assets.addAll(panels);
            // update metadata
            experience.setAssets(assets);
        } catch (Throwable e) {
            log.error("Error parsing experience XML: " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("Error closing experience XML stream");
            }
        }
        return experience;
    }

    protected List<Asset> getVideoScenario(Document experienceDocument) throws Throwable {
        return parsePath(experienceDocument, VIDEOS_PATH, AssetType.VIDEO);
    }

    protected List<Asset> getImageScenario(Document experienceDocument) throws Throwable {
        return parsePath(experienceDocument, IMAGES_PATH, AssetType.IMAGE);
    }

    protected List<Asset> getHotspot(Document experienceDocument) throws Throwable {
        return parsePath(experienceDocument, HOTSPOT_PATH, AssetType.HOTSPOT);
    }

    protected List<Asset> getSubtitle(Document experienceDocument) throws Throwable {
        return parsePath(experienceDocument, SUBTITLE_PATH, AssetType.SUBTITLE);
    }

    protected List<Asset> getDetailsPanel(Document experienceDocument) throws Throwable {
        return parsePath(experienceDocument, DETAILS_PANEL_PATH, AssetType.DETAILS_PANEL);
    }

    private List<Asset> parsePath(Document experienceDocument, String xmlPath, AssetType type) throws XPathExpressionException {
        List<Asset> assets = new ArrayList<>();
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList =
            (NodeList) xPath.compile(xmlPath).evaluate(experienceDocument, XPathConstants.NODESET);

        if (null != nodeList) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String id = null;
                // the list with the properties
                Map<String, String> properties = new HashMap<>();
                Node node = nodeList.item(i);
                // System.out.println(node.getNodeName()); // always 'VideoScenario'
                NamedNodeMap attributes = node.getAttributes();
                // String id = attributes.getNamedItem("id").getNodeValue(); // punto l'ID
                if (attributes.getNamedItem("id") != null) {
                    id = attributes.getNamedItem("id").getNodeValue();
                }
                for (int j = 0; j < attributes.getLength(); j++) {
                    Node attribute = attributes.item(j);
                    // System.out.println("\t" + attribute.getNodeName() + " : " +attribute.getNodeValue());
                    properties.put(attribute.getNodeName(), attribute.getNodeValue());
                }
                // create asset from the properties
                Asset asset = new Asset();

                // id
                asset.setId(id);
                // filename
                if (properties.containsKey(KEY_SRC)
                    || properties.containsKey(KEY_IMAGESRC)) {
                    Path path = properties.containsKey(KEY_SRC) ?
                        Paths.get(properties.get(KEY_SRC)) : Paths.get(properties.get(KEY_IMAGESRC));
                    if (path.getFileName() != null) {
                        Path filename = path.getFileName();
                        asset.setAssetFilename(filename.toString()); // FILE
                    } else {
                        log.warn("WARNING: detected scenario or hotspot with NULL src");
                    }
                }
                // type
                asset.setType(type);
                // thumbnail
                if (properties.containsKey(KEY_THUMBNAIL)) {
                    Path path = Paths.get(properties.get(KEY_THUMBNAIL));
                    if (path.getFileName() != null) {
                        Path filename = path.getFileName();
                        asset.setThumbnailFilename(filename.toString()); // FILE
                    }
                }
                if (properties.containsKey(KEY_TITLE)) { // Image, Video
                    asset.setTitle(properties.get(KEY_TITLE));
                }
                if (properties.containsKey(KEY_LABEL)) { // Hotspot
                    asset.setTitle(properties.get(KEY_LABEL));
                }
                // finally
                assets.add(asset);
            }
        }
        return assets;
    }

    /**
     * Extract name and description as well as age profile from the experience script
     * @param experience the experience object to modify
     * @param experienceDocument the parsed document to analyse
     */
    protected void extractMetadata(Experience experience, Document experienceDocument) {
        List<Profile> profiles = new ArrayList<>();

        try {
            if (experience != null
                && experienceDocument != null) {
                XPath xPath = XPathFactory.newInstance().newXPath();
                NodeList nodeList = (NodeList) xPath.compile(NAMEDESCR_PATH).evaluate(experienceDocument, XPathConstants.NODESET);
                Node tmp;
                // name
                if (StringUtils.isBlank(experience.getName())
                    && nodeList.item(0) != null
                    && nodeList.item(0).getAttributes() != null
                    && (tmp = nodeList.item(0).getAttributes().getNamedItem(KEY_NAME)) != null) {
                    String name = tmp.getNodeValue();
                    log.debug("Name '{}' assigned from experience ", name);
                    experience.setName(name);
                }
                // description
                if (StringUtils.isBlank(experience.getDescription())
                    && nodeList.item(0) != null
                    && nodeList.item(0).getAttributes() != null
                    && (tmp = nodeList.item(0).getAttributes().getNamedItem(KEY_DESCRIPTION)) != null) {
                    String description = tmp.getNodeValue();
                    log.debug("Description '{}' assigned from experience ", description);
                    experience.setDescription(description);
                }
                // profile
                nodeList = (NodeList) xPath.compile(PROFILE_PATH).evaluate(experienceDocument, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    tmp = nodeList.item(i);
                    String ageStr = tmp.getTextContent();
                    Age age = Age.codeOf(ageStr);

                    if (age != null) {
                        Profile profile = new Profile();
                        log.debug("Adding AGE {} to profile", age);
                        profile.setAge(age);
                        profiles.add(profile);
                    }
                }
                if (!profiles.isEmpty()) {
                    MultiProfile mp = new MultiProfile();
                    mp.setProfiles(profiles);
                    experience.setProfiles(mp);
                }
            }
        } catch (Throwable e) {
            log.error("error getting name and description form XML", e);
        }
    }

    @Override
    public Experience save(Experience experience) {
        boolean proceed = true;
        final String relativePath = String.valueOf(System.currentTimeMillis());
        final ZonedDateTime creationTime = ZonedDateTime.now();

        // persist files
        if (experience.getMultipartXmlFile() != null || experience.getMultipartThumbnailFile() != null) {
            try {
                if (experience.getMultipartXmlFile() != null && !experience.getMultipartXmlFile().isEmpty()) {
                    fileStorageService.save(experience.getMultipartXmlFile(), relativePath);
                    log.debug("Saved experience " + experience.getName() + " XML " + experience.getXml() + " in relative path " + relativePath);
                }
                if (experience.getMultipartThumbnailFile() != null && !experience.getMultipartThumbnailFile().isEmpty()) {
                    fileStorageService.save(experience.getMultipartThumbnailFile(), relativePath);
                    log.debug("Saved experience " + experience.getName() + " thumbnail " + experience.getThumbnail() + " in relative path " + relativePath);
                }
            } catch (Throwable t) {
                log.error("Error persisting experience file on disk: " + t.getMessage());
                proceed = false;
            }
        } else {
            log.debug("no XML or thumbnail detected");
        }
        try {
            if (proceed) {
                if (experience.getId() == null) {
                    // set upload / creation time
                    experience.setUpload(creationTime);
                    // set the path for the assets
                    experience.setPath(relativePath);
                    // set the experience master file name
                    if (experience.getMultipartXmlFile() != null && !experience.getMultipartXmlFile().isEmpty()) {
                        experience.setXml(experience.getMultipartXmlFile().getOriginalFilename());
                    }
                    if (experience.getMultipartThumbnailFile() != null && !experience.getMultipartThumbnailFile().isEmpty()) {
                        experience.setThumbnail(experience.getMultipartThumbnailFile().getOriginalFilename());
                    }
                    // set status
                    experience.setStatus(ExperienceStatus.DRAFT);
                }
                // fill in name description etc. if needed
                parse(experience);
                // persist
                experience = experienceRepository.save(experience);
            }
        } catch (Throwable t) {
            log.error("Error saving the experience", t);
            throw t;
        }
        return experience;
    }

    /**
     * Return the path under which the assets are saved
     * NOTE: this does NOT parse the XML!
     * @param id of the experience
     * @return the path under which the assets are saved
     */
    protected Path getExperienceBasePath(Long id) {
        Path path = null;

        if (id != null) {
            Optional<Experience> expOpt = experienceRepository.findById(id);
            if (expOpt.isPresent()) {
                path = getExperienceBasePath(expOpt.get());
            }
        }
        return path;
    }

    /**
     * Return the path under which the assets are saved
     * NOTE: this does NOT parse the XML!
     * @param experience the entity
     * @return the path under which the assets are saved
     */
    protected Path getExperienceBasePath(Experience experience) {
        Path path = null;

        if (null != experience) {
            Path uploadDir = fileStorageService.getUploadDir();
            path = uploadDir.resolve(experience.getPath());
        }
        return path;
    }

    @Override
    public void delete(Long id) {
        try {
            Path experienceResourcePath = getExperienceBasePath(id);
            if (experienceResourcePath != null) {
                experienceRepository.deleteById(id);
                log.debug("Record deleted successfully");
                Files.walk(experienceResourcePath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
                Files.deleteIfExists(experienceResourcePath);
            }
        } catch (Throwable t) {
            log.error("Error deleting experience: " + t.getMessage());
        }
    }

    @Override
    public Page<Experience> findAll(Pageable pageable) {
        log.debug("Request to get all devices");
        return experienceRepository.findAll(pageable);
    }

    @Override
    public Optional<Experience> load(Long id) {
        log.debug("Request to get and parse experience : {}", id);
        Optional<Experience> opt = experienceRepository.findById(id);
        opt.ifPresent(e -> parse(e));
        return opt;
    }

    @Override
    public Optional<Experience> get(Long id) {
        log.debug("Request to get experience : {}", id);
        return experienceRepository.findById(id);
    }

    @Override
    public List<Asset> saveAsset(MultipartFile file, MultipartFile thumb, Long id, boolean isAsset) {
        List<Asset> assets = null;

        if (null != id) {
            boolean updated = false;

            try {
                // load the experience...
                Optional<Experience> opt = load(id);
                if (opt.isPresent()) {
                    Experience experience = opt.get();

                    // XML and/or experience thumbnail
                    if (!isAsset) {
                        // update experience file...
                        if (file != null && !file.isEmpty() && Objects.equals(file.getOriginalFilename(), experience.getXml())) {
                            log.debug("Experience file update detected: {}", experience.getXml());
                            // overwrite the original
                            fileStorageService.overwrite(file, experience.getPath());
                            updated = true;
                        }
                        // ...update the thumbnail file...
                        if (thumb != null && !thumb.isEmpty() && (StringUtils.isBlank(experience.getThumbnail()) || Objects.equals(thumb.getOriginalFilename(), experience.getThumbnail()))) {
                            log.debug("Experience thumbnail update detected: {}", experience.getThumbnail());
                            fileStorageService.overwrite(thumb, experience.getPath());
                            updated = true;
                        }
                        if (updated) {
                            // delete outdated assets, this will refresh assets list as well
                            deleteUnnecessaryAsset(experience);
                        }
                    } else {
                        // asset and/or asset thumbnail
                        if (file != null && experience.isExpectedAsset(file.getOriginalFilename())) { // is the file expected?
                            log.debug(file.getOriginalFilename() + " is a EXPECTED asset for this resource");
                            // save the resource (and the thumbnail if provided)
                            fileStorageService.overwrite(file, experience.getPath());
                            // finally
                            updated = true;
                        } else {
                            if (file != null) {
                                log.debug("asset file " + file.getOriginalFilename() + " is a asset NOT EXPECTED for this resource");
                            }
                        }
                        if (thumb != null && experience.isExpectedAsset(thumb.getOriginalFilename())) { // is the file expected?
                            log.debug(thumb.getOriginalFilename() + " is a EXPECTED asset for this resource");
                            // save the thumbnail
                            fileStorageService.overwrite(thumb, experience.getPath());
                            // finally
                            updated = true;
                        } else {
                            if (thumb != null) {
                                log.debug("thumbnail " + thumb.getOriginalFilename() + " is a asset NOT EXPECTED for this resource");
                            }
                        }
                        if (updated) {
                            // refresh assets (need updated present on disk flag)
                            parse(experience);
                        }
                    }
                    if (updated) {
                        assets = experience.getAssets();
                    }
                } else {
                    String name = "unknown";

                    if (file != null && !file.isEmpty()) {
                        name = file.getOriginalFilename();
                    }
                    if (thumb != null && !thumb.isEmpty()) {
                        name = thumb.getOriginalFilename();
                    }

                    log.warn("could not associate asset '" + name + "' to unknown scenario " + id);
                }
            } catch (Throwable t) {
                log.error("Error adding asset " + t.getMessage());
                throw t;
            }
        }
        return assets;
    }

    @Override
    public File getAssetFile(Long id, String assetId, AssetType type) {
        return getAssetFile(id, assetId, type, false);
    }

    @Override
    public File getAssetThumbnail(Long id, String assetId, AssetType type) {
        return getAssetFile(id, assetId, type, true);
    }

    protected File getAssetFile(Long id, String assetId, AssetType type, boolean isThumbnail) {
        File file = null;

        if (null != id) {
            log.debug("looking for asset id {} of type {} (null == any)", id, type);
            Optional<Experience> opt = load(id);
            if (opt.isPresent()) {
                Experience experience = opt.get();

                // get the base directory of the experience in the filesystem
                Path experiencePath = getExperienceBasePath(experience);
                // find the asset
                Asset asset = (type == null) ? experience.findAssetById(assetId) : experience.findAssetByIdAndType(assetId, type);
                if (asset != null) {
                    String filenameStr = isThumbnail ? asset.getThumbnailFilename() : asset.getAssetFilename();
                    if (StringUtils.isNotBlank(filenameStr)) {
                        Path filename = Paths.get(filenameStr);
                        file = getFile(experiencePath, filename);
                    } else {
                        log.debug("Asset " + assetId + " belonging to experience " + id + " found, but no file defined" );
                    }
                } else {
                    log.debug("Unknown asset " + assetId + " for experience " + id);
                }
            } else {
                log.debug("Unknown experience id " + id + " to get assets from");
            }
        }
        return file;
    }

    @Override
    public File getExperienceXml(Long id) {
        return getExperienceMainAsset(id, false);
    }

    @Override
    public File getExperienceThumbnail(Long id) {
        return getExperienceMainAsset(id, true);
    }

    @Override
    public Optional<ExperienceDTO> describe(Long id) {
        ExperienceDTO dto = null;

        if (null != id) {
            Optional<Experience> opt = load(id);
            if (opt.isPresent()) {
                Experience experience = opt.get();
                dto= new ExperienceDTO();

                dto.setName(experience.getName());
                dto.setDescription(experience.getDescription());
                dto.setStatus(experience.getStatus());
                dto.setId(id);
                if (experience.getProfiles() != null
                    && experience.getProfiles().getProfiles() != null) {
                    dto.setProfiles(experience.getProfiles().getProfiles());
                }
                // create list of assets
                dto.setAssets(experience.getAssets());
            } else {
                log.debug("Unknown experience id " + id + " to get master XML from");
            }
        }
        return Optional.ofNullable(dto);
    }

    @Override
    public List<ExperienceSummaryDTO> findExperienceByAge(Age age) {
        List<ExperienceSummaryDTO> list = null;

        if (null != age) {
            list = experienceRepository.profilesContainsAge(age.getCode());
        }
        return list;
    }

    @Override
    public List<ExperienceSummaryDTO> findExperienceByAge(AccessCode accessCode) {
        List<ExperienceSummaryDTO> list = null;

        if (accessCode != null
            && accessCode.getProfile() != null) {
            list = findExperienceByAge(accessCode.getProfile().getAge());
        }
        return list;
    }

    @Override
    public Boolean changeExperienceStatus(Long id, ExperienceStatus status) {
        final List<Boolean> result = new ArrayList<>(Arrays.asList(Boolean.FALSE));

        if (id != null && status != null) {
            // no need to parse experience XML
            Optional<Experience> opt = experienceRepository.findById(id);
            opt.ifPresent(e -> {
                log.debug("Changing status of the experience {} in {}", id, status);
                e.setStatus(status);
                experienceRepository.save(e);
                result.set(0, true);
            });
            if (!opt.isPresent()) {
                log.debug("Couldn't change status of unknown experience {}", id);
            }
            return result.get(0);
        }
        return result.get(0);
    }

    @Override
    public List<ExperienceSummaryDTO> getExperiences() {
        return experienceRepository.getExperiencesSummary();
    }

    @Override
    public List<ExperienceSummaryDTO> getExperienceByStatus(Integer status) {
        List<ExperienceSummaryDTO> result = new ArrayList<>();
        if (status != null) {
            result = experienceRepository.findExperienceByStatusSummary(status);
        }
        return result;
    }


    /**
     * Get the experience file or the thumbnail of the given experience
     * @param id the unique identifier of the experience
     * @param isThumbnail when true selects the thumbnail
     * @return the XML of the experience file or the thumbnail associated
     */
    protected File getExperienceMainAsset(Long id, boolean isThumbnail) {
        File file = null;

        if (null != id) {
            Optional<Experience> opt = experienceRepository.findById(id);
            if (opt.isPresent()) {
                Experience experience = opt.get();
                // get the file name from the src element
                Path experiencePath = getExperienceBasePath(experience);
                Path filename = null;
                if (!isThumbnail && StringUtils.isNotBlank(experience.getXml())) {
                    log.debug("Requesting master XML file");
                    filename = Paths.get(experience.getXml());
                } else if (isThumbnail && StringUtils.isNotBlank(experience.getThumbnail())) {
                    log.debug("Requesting master thumbnail file");
                    filename = Paths.get(experience.getThumbnail());
                } else {
                    log.debug("Unknown master file for experience " + id + " (thumbnail: " + isThumbnail + ")");
                }
                if (null != filename) {
                    file = getFile(experiencePath, filename);
                }
            } else {
                log.debug("Unknown experience id " + id + " to get master XML from");
            }
        }
        return file;
    }

    protected List<Asset> deleteUnnecessaryAsset(Experience exp) {
        List<Asset> existing = new ArrayList<>();

        // save existing
        exp.getAssets().forEach(a -> existing.add(a.clone()));
        // recreate list
        parse(exp);
        // check whether the old asset exists in the new exp
        if (!existing.isEmpty()) {
            existing.forEach(a -> {
                Asset currentAsset = exp.findAssetByIdAndType(a.getId(), a.getType());

                if (currentAsset == null && a.assetPresentOnDisk) {
                    log.debug("must delete asset '{}' : {}", a.getId(), a.getAssetFilename());
                    Path basePath = getExperienceBasePath(exp);
                    Path assetPhysPath = getFile(basePath, Paths.get(a.getAssetFilename())).toPath();
                    Path thumbPhysPath = StringUtils.isNotBlank(a.getThumbnailFilename()) ?
                        getFile(basePath, Paths.get(a.getThumbnailFilename())).toPath() : null;
                    try {
                        Files.delete(assetPhysPath);
                        log.debug("asset {} deleted", assetPhysPath);
                        if (thumbPhysPath != null) {
                            log.debug("deleting {} (if exists)", thumbPhysPath);
                            Files.deleteIfExists(thumbPhysPath);
                            log.debug("asset's thumbnail {} deleted", thumbPhysPath);
                        }
                    } catch (IOException e) {
                        log.error("error deleting " + assetPhysPath, e);
                    }
                } else {
                    log.debug("skipping '{}' not present on disk", a.getId());
                }
            });
        }
        return exp.getAssets();
    }


    /**
     * Get the file given the base directory of the experience and the filename
     * @param experiencePath base directory
     * @param filename filename desired
     * @return the file requested
     */
    protected File getFile(Path experiencePath, Path filename) {
        Path physPath = experiencePath.resolve(filename);

        log.debug("Checking " + physPath + " for read");
        return fileStorageService.getFile(physPath);
    }

    public static final String VIDEOS_PATH = "/Video360Config/VideoScenario";
    public static final String IMAGES_PATH = "/Video360Config/ImageScenario";
    public static final String HOTSPOT_PATH = "/Video360Config/HotspotElement";
    public static final String SUBTITLE_PATH = "/Video360Config/SubtitleElement";
    public static final String DETAILS_PANEL_PATH = "/Video360Config/DetailsPanelElement";
    public static final String NAMEDESCR_PATH = "/Video360Config";
    public static final String PROFILE_PATH = "/Video360Config/Tag";
}
