package it.iedx.login.service.impl;

import it.iedx.login.domain.POI;
import it.iedx.login.repository.POIRepository;
import it.iedx.login.service.POIService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class POIServiceImpl implements POIService {

    private final Logger log = LoggerFactory.getLogger(POIServiceImpl.class);

    private final POIRepository poirepository;
    private final FileStorageServiceImpl fileStorageService;

    public POIServiceImpl(POIRepository poirepository, FileStorageServiceImpl storageService) {
        this.poirepository = poirepository;
        this.fileStorageService = storageService;
    }

    @Override
    public POI save(POI poi) {
        POI saved = null;

        if (poi != null) {
            if (poi.getFile() != null ) {
                MultipartFile file = poi.getFile();

                fileStorageService.overwrite(file, POI_DIRECTORY);
                poi.setSrc(file.getOriginalFilename());
                poi.setIcon(file.getOriginalFilename());
            }
            saved = poirepository.save(poi);
        }
        return saved;
    }

    @Override
    public File getPOIImage(Long id) {
        File result = null;
        log.debug("POI #{} image requested", id);
        Optional<POI> opt = poirepository.findById(id);
        if (opt.isPresent()) {
            Path physPath = getPoiImagePath(opt);
            result = fileStorageService.getFile(physPath);
        }
        return result;
    }

    @Override
    public List<POI> findAll() {
        log.debug("Getting the list of POI");
        return poirepository.findAll();
    }

    @Override
    public void delete(Long id) {
        log.debug("delete #{} ", id);
        Optional<POI> opt = poirepository.findById(id);
        try {
            if (opt.isPresent()) {
                Path img = getPoiImagePath(opt);
                if (img != null) {
                    log.debug("deleting POI image {}", img);
                    Files.deleteIfExists(img);
                }
                log.debug("deleting POI entity");
                poirepository.deleteById(id);
            }
        } catch (Throwable t) {
            log.error("error deleting POI", t);
        }
    }

    private Path getPoiImagePath(Optional<POI> opt) {
        Path result = null;

        if (opt.isPresent()) {
            String filename = opt.get().getIcon();

            if (StringUtils.isNotBlank(filename)) {
                Path file = Paths.get(filename);
                Path basePath = fileStorageService.getUploadDir();
                Path poiPath = basePath.resolve(POI_DIRECTORY);
                result = poiPath.resolve(file);
            }
        }
        return result;
    }

    public final static String POI_DIRECTORY = "POI";
}
