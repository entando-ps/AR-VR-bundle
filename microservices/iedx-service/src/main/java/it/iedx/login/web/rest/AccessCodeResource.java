package it.iedx.login.web.rest;


import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.AssetType;
import it.iedx.login.domain.Device;
import it.iedx.login.domain.Event;
import it.iedx.login.domain.Experience;
import it.iedx.login.domain.ExperienceStatus;
import it.iedx.login.domain.LeaseStatus;
import it.iedx.login.domain.Location;
import it.iedx.login.domain.POI;
import it.iedx.login.domain.Profile;
import it.iedx.login.service.AccesscodeQueryService;
import it.iedx.login.service.AccesscodeService;
import it.iedx.login.service.DeviceService;
import it.iedx.login.service.EventService;
import it.iedx.login.service.ExperienceService;
import it.iedx.login.service.POIService;
import it.iedx.login.service.PairingService;
import it.iedx.login.service.dto.AccessCodeResponseDTO;
import it.iedx.login.service.dto.Asset;
import it.iedx.login.service.dto.DeviceRequestDTO;
import it.iedx.login.service.dto.ExperienceDTO;
import it.iedx.login.service.dto.ExperienceSummaryDTO;
import it.iedx.login.service.dto.LoginRequestDTO;
import it.iedx.login.service.dto.LoginResponseDTO;
import it.iedx.login.service.dto.PingDTO;
import it.iedx.login.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link AccessCode}.
 */
@RestController
@RequestMapping("/api")
public class AccessCodeResource {

    private final Logger log = LoggerFactory.getLogger(AccessCodeResource.class);

    private static final String ENTITY_NAME = "experience";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccesscodeService accesscodeService;
    private final AccesscodeQueryService accesscodeQueryService;
    private final PairingService pairingService;
    private final ExperienceService experienceService;
    private final EventService eventService;
    private final POIService poiService;

    @Autowired
    private DeviceService deviceService;

    public AccessCodeResource(AccesscodeService accesscodeService, AccesscodeQueryService accesscodeQueryService, PairingService pairingService, ExperienceService experienceService, EventService eventService, POIService poiService) {
        this.accesscodeService = accesscodeService;
        this.accesscodeQueryService = accesscodeQueryService;
        this.pairingService = pairingService;
        this.experienceService = experienceService;
        this.eventService = eventService;
        this.poiService = poiService;
    }

    /*
     * {@code DELETE  /experience/:id} : delete the "id" experience.
     *
     * @param id the id of the accesscodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/experience/{id}")
    public ResponseEntity<Void> deleteExpericence(@PathVariable Long id) {
        log.debug("REST request to delete experience : {}", id);
        experienceService.delete(id);
        return ResponseEntity.noContent().
            headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/accesscode")
    public ResponseEntity<AccessCodeResponseDTO> requestAccessCode(@RequestBody Profile profile) {
        log.debug("REST request for a new AccessCode {}", profile);

        AccessCode ac = pairingService.create(profile);

        AccessCodeResponseDTO dto = new AccessCodeResponseDTO();
        dto.setCode(ac.getCode());
        dto.setMessage(ac.getMessage());
        dto.setQrcode(ac.getQrcode());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginByUid(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO result = null;

        if (loginRequest != null) {
            String deviceId = loginRequest.getToken();

            result = pairingService.consumeByDeviceId(deviceId);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/ext/login")
    public ResponseEntity<LoginResponseDTO> loginByQr(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO result = null;

        if (loginRequest != null) {
            final String accessCode = loginRequest.getToken();

            result = pairingService.consumeByAccessCode(accessCode);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/accesscode/{id}")
    public ResponseEntity<PingDTO> updateAccessCodeStatus(@RequestBody PingDTO status, @PathVariable Long id) {
        ResponseEntity<PingDTO> resp;

        log.debug("updating access code to status {}", status);
        if (accesscodeService.findOne(id).isPresent()) {
            accesscodeService.updateStatus(id, status.getStatus());
            PingDTO dto = new PingDTO();

            dto.setStatus(status.getStatus());
            dto.setId(id);
            resp = ResponseEntity.ok(dto);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return resp;
    }


    @PostMapping("/experience")
    public ResponseEntity<ExperienceDTO> newExperience(@RequestParam(value = "xmlFile") MultipartFile xmlFile,
                                                       @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
                                                       @RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "description", required = false) String description) {

        // crea oggetto esperienza
        Experience experience = new Experience(xmlFile, thumbnailFile, name, description);
        experience = experienceService.save(experience);
        final ExperienceDTO dto = new ExperienceDTO();

        dto.setId(experience.getId()); // FIXME modificare dto per eseguire una copia delle property con BeanUtils
        dto.setName(experience.getName());
        dto.setDescription(experience.getDescription());
        dto.setAssets(experience.getAssets());
        if (experience.getProfiles() != null && !experience.getProfiles().getProfiles().isEmpty()) {
            dto.setProfiles(experience.getProfiles().getProfiles());
        }
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/experience/{id}/asset")
    public ResponseEntity<Object> updateExperiences(@RequestParam(value = "file", required = false) MultipartFile file,
                                                  @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
                                                  @PathVariable Long id,
                                                  @RequestParam(required = false) Boolean isAsset) {
        List<Asset> assets;
        if (isAsset == null) {
            isAsset = true;
        }
        if ((assets = experienceService.saveAsset(file, thumbnailFile, id, isAsset)) != null) {
            ExperienceDTO dto = new ExperienceDTO();

            dto.setAssets(assets);
            return ResponseEntity.ok(dto);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/experience/{id}/asset/{assetId}")
    public @ResponseBody ResponseEntity<Resource> getAsset(@PathVariable Long id, @PathVariable String assetId, @RequestParam(required = false) AssetType type) throws IOException {
        File file = experienceService.getAssetFile(id, assetId, type);

        return getResourceResponseEntity(file);
    }

    @GetMapping(value = "/experience/{id}/asset/thumbnail/{assetId}")
    public @ResponseBody ResponseEntity<Resource> getAssetThumbnail(@PathVariable Long id, @PathVariable String assetId, @RequestParam(required = false) AssetType type) throws IOException {
        File file = experienceService.getAssetThumbnail(id, assetId, type);

        return getResourceResponseEntity(file);
    }

    @GetMapping(value = "/experience/{id}/xml")
    public @ResponseBody ResponseEntity<Resource> getExperienceXml(@PathVariable Long id) throws IOException {
        File file = experienceService.getExperienceXml(id);

        return getResourceResponseEntity(file);
    }

    @GetMapping(value = "/experience/{id}/thumbnail")
    public @ResponseBody ResponseEntity<Resource> getExperienceThumbnail(@PathVariable Long id) throws IOException {
        File file = experienceService.getExperienceThumbnail(id);

        return getResourceResponseEntity(file);
    }

    protected ResponseEntity<Resource> getResourceResponseEntity(File file) throws IOException {

        if (file != null) {
            MediaType mime;
            HttpHeaders headers = new HttpHeaders();
            Path path = Paths.get(file.getAbsolutePath());

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            // set MIME type
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String mimeType = fileNameMap.getContentTypeFor(file.getName());
            // set content disposition "Content-disposition", "attachment; filename="+ fileName);
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(file.getName())
                .build();
            // set header
            headers.setContentDisposition(contentDisposition);
            if (StringUtils.isNotBlank(mimeType)) {
                String[] token = mimeType.split("/");
                mime = new MediaType(token[0], token[1]);
            } else {
                mime = MediaType.APPLICATION_OCTET_STREAM;
            }
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mime)
                .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/experience/{id}/describe")
    public @ResponseBody ResponseEntity<ExperienceDTO> loadExperience(@PathVariable Long id) {
        Optional<ExperienceDTO> opt = experienceService.describe(id);

        return ResponseUtil.wrapOrNotFound(opt);
    }

    @GetMapping(value = "/experience/{id}")
    public @ResponseBody ResponseEntity<Experience> getExperience(@PathVariable Long id) {
        Optional<Experience> opt = experienceService.get(id);
        return ResponseUtil.wrapOrNotFound(opt);
    }

    @PutMapping("/experience")
    public ResponseEntity<Experience> updateExperience(@RequestBody Experience experience) {
        log.debug("REST request to update experience : {}", experience);
        if (experience.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "device", "idnull");
        }
        experience = experienceService.save(experience);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, "device", experience.getId().toString()))
            .body(experience);
    }

    @PutMapping(value = "/experience/{id}/publish")
    public @ResponseBody
    ResponseEntity<Boolean> publishExperience(@PathVariable Long id) throws IOException {
        Boolean success = experienceService.changeExperienceStatus(id, ExperienceStatus.PUBLISHED);
        Optional<Boolean> opt = Optional.ofNullable(success);
        return ResponseEntity.of(opt);
    }

    @PutMapping(value = "/experience/{id}/retire")
    public @ResponseBody
    ResponseEntity<Boolean> retireExperience(@PathVariable Long id) {
        Boolean success = experienceService.changeExperienceStatus(id, ExperienceStatus.DRAFT);
        Optional<Boolean> opt = Optional.ofNullable(success);
        return ResponseEntity.of(opt);
    }

    @GetMapping("/experiences")
    public ResponseEntity<List<ExperienceSummaryDTO>> getExperience(@RequestParam(required = false) Integer status) {
        List<ExperienceSummaryDTO> result;

        if (status != null) {
            log.debug("searching for experiences by status {}", status);
            result = experienceService.getExperienceByStatus(status);
        } else {
            result = experienceService.getExperiences();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/device")
    public ResponseEntity<Device> newDevice(@RequestBody DeviceRequestDTO device) {
        Device dev = null;

        if (device != null) {
            dev = device.toDevice();
            dev = deviceService.save(dev);
        }
        return ResponseEntity.ok(dev);
    }

    @PutMapping("/device")
    public ResponseEntity<Device> updateDevice(@RequestBody Device device) {
        log.debug("REST request to update Device : {}", device);
        if (device.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "device", "idnull");
        }
        device = deviceService.save(device);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, "device", device.getId().toString()))
            .body(device);
    }

    @GetMapping("/device/{id}")
    public ResponseEntity<Device> geteDevice(@PathVariable Long id) {
        log.debug("REST request to get Device : {}", id);
        Optional<Device> device = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(device);
    }

    @DeleteMapping("/device/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.debug("REST request to delete Device : {}", id);
        deviceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, "device", id.toString())).build();
    }

    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getDevices() {
        log.debug("REST request for list of devices");
        List<Device> result  = deviceService.findAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/event")
    public ResponseEntity<Event> newEvent(@RequestBody Event event) {
        Event evt;

        log.debug("REST request to track an event");
        if (event != null) {
            if ((evt = eventService.save(event)) != null) {
                return ResponseEntity.ok(evt);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/poi")
    public ResponseEntity<POI> newPOI(@RequestParam(value = "file", required = false) MultipartFile file,
                                      @RequestParam Double lat,
                                      @RequestParam Double lng,
                                      @RequestParam String name,
                                      @RequestParam String link,
                                      @RequestParam(required = false) String src,
                                      @RequestParam String info) {
        log.debug("Request to create POI {}", name);
        POI poi = new POI();
        Location location = new Location();

        location.setLat(lat);
        location.setLng(lng);
        return createPoiEntity(file, name, link, src, info, poi, location);
    }

    @PutMapping("/poi/{id}")
    public ResponseEntity<POI> updatePOI(@RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestParam Double lat,
                                         @RequestParam Double lng,
                                         @RequestParam String name,
                                         @RequestParam String link,
                                         @RequestParam(required = false)  String src,
                                         @RequestParam String info,
                                         @PathVariable Long id) {
        log.debug("Request to update POI {}", name);
        POI poi = new POI();
        Location location = new Location();

        location.setLat(lat);
        location.setLng(lng);
        poi.setId(id);
        return createPoiEntity(file, name, link, src, info, poi, location);
    }

    @NotNull
    protected ResponseEntity<POI> createPoiEntity(@RequestParam(value = "file", required = false) MultipartFile file,
                                                  @RequestParam String name,
                                                  @RequestParam String link,
                                                  @RequestParam String src,
                                                  @RequestParam String info,
                                                  POI poi, Location location) {
        poi.setName(name);
        poi.setLink(link);
        poi.setSrc(src);
        poi.setInfo(info);
        poi.setFile(file);
        poi.setLocation(location);
        poi = poiService.save(poi);
        return ResponseEntity.ok(poi);
    }

    @GetMapping("/pois")
    public ResponseEntity<List<POI>> getPOI() {
        log.debug("REST request to get the list of POI");
        List<POI> pois = poiService.findAll();
        return ResponseEntity.ok(pois);
    }

    @GetMapping(value = "/poi/{id}/image")
    public @ResponseBody ResponseEntity<Resource> getPoiImage(@PathVariable Long id) throws IOException {
        log.debug("REST request to get POI #{} image", id);
        File file = poiService.getPOIImage(id);
        return getResourceResponseEntity(file);
    }

    @DeleteMapping("/poi/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable Long id) {
        log.debug("REST request to delete POI : {}", id);
        poiService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, "POI", id.toString())).build();
    }

    @GetMapping("/ping/{id}")
    public ResponseEntity<PingDTO> keepAlive(@PathVariable Long id) {
        ResponseEntity<PingDTO> resp = null;
        log.debug("keep alive request for access code {}", id);
        LeaseStatus lease = accesscodeService.keepAlive(id);
        if (lease != null) {
            PingDTO dto = new PingDTO();

            dto.setId(id);
            dto.setStatus(lease);
            resp = ResponseEntity.ok(dto);
        } else {
            resp = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return resp;
    }
}
