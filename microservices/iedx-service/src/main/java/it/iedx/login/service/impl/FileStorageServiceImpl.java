package it.iedx.login.service.impl;

import it.iedx.login.config.ApplicationProperties;
import it.iedx.login.service.FileStorageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    private Path uploadDir;

    public FileStorageServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    public void init() {
        try {
            if (null != applicationProperties
                && null != applicationProperties.getFileTransfer()) {
                String uploadDirStr = applicationProperties.getFileTransfer().getUploadDir();
                if (StringUtils.isNotBlank(uploadDirStr)) {
                    uploadDir = Paths.get(uploadDirStr);

                    if (Files.notExists(uploadDir)) {
                        log.debug("created new base directory");
                        Files.createDirectory(uploadDir);
                    }
                    log.info("Starting file service with BASE directory: " + uploadDir);
                } else {
                    throw new RuntimeException("Incorrect FileTransfer configuration detected");
                }
            } else {
                throw new RuntimeException("FileTransfer configuration missing");
            }
        } catch (Throwable t) {
            log.error("Critical error while configuring file service detected", t);
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Path filename = Paths.get(file.getOriginalFilename());
            Path filePhysicalPath = uploadDir.resolve(filename);
            log.debug("Saving resource in " + filePhysicalPath.toAbsolutePath());
            Files.copy(file.getInputStream(), filePhysicalPath);
            log.debug("save completed");
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void save(MultipartFile file, String directory) {
        writeOnDisk(file, directory, false);
    }

    @Override
    public void save(byte[] data, String filename, String directory) {
        writeOnDisk(data, filename, directory, false);
    }


    @Override
    public void overwrite(MultipartFile file, String directory) {
        writeOnDisk(file, directory, true);
    }

    @Override
    public void overwrite(byte[] data, String filename, String directory) {
        writeOnDisk(data, filename, directory, true);
    }

    protected void writeOnDisk(MultipartFile file, String directory, boolean overwrite) {
        try {
            Path relativeDir = Paths.get(directory);
            Path filename = Paths.get(file.getOriginalFilename());
            Path currentUploadDir = uploadDir.resolve(relativeDir);
            if (Files.notExists(currentUploadDir)) {
                Files.createDirectory(currentUploadDir);
            }
            Path filePhysicalPath = currentUploadDir.resolve(filename);
            if (overwrite || Files.notExists(filePhysicalPath)) {
                // delete the existing file if it already exists
                if (overwrite && Files.exists(filePhysicalPath)) {
                    log.debug("Deleting " + filePhysicalPath.toAbsolutePath());
                    Files.delete(filePhysicalPath);
                }
                log.debug("Saving resource in " + filePhysicalPath.toAbsolutePath());
                Files.copy(file.getInputStream(), filePhysicalPath);
                log.debug("save completed");
            }
        } catch(Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void writeOnDisk(byte[] data, String name, String directory, boolean overwrite) {
        try {
            Path relativeDir = Paths.get(directory);
            Path filename = Paths.get(name);
            Path currentUploadDir = uploadDir.resolve(relativeDir);
            if (Files.notExists(currentUploadDir)) {
                Files.createDirectory(currentUploadDir);
            }
            Path filePhysicalPath = currentUploadDir.resolve(filename);
            if (overwrite || Files.notExists(filePhysicalPath)) {
                // delete the existing file if it already exists
                if (overwrite && Files.exists(filePhysicalPath)) {
                    log.debug("Deleting " + filePhysicalPath.toAbsolutePath());
                    Files.delete(filePhysicalPath);
                }
                log.debug("Saving resource in " + filePhysicalPath.toAbsolutePath());
                try (OutputStream stream = new FileOutputStream(filePhysicalPath.toFile())) {
                    stream.write(data);
                }
                log.debug("save completed");
            }
        } catch (Throwable t) {
            log.error("error writing on disk", t);
        }
    }

    @Override
    public File getFile(Path physPath) {
        File file = null;

        if (Files.exists(physPath)) {
            log.debug("returning " + physPath + " for IO operations");
            file = physPath.toFile();
        } else {
            log.debug("File " + physPath + " not present or not readable");
        }
        return file;
    }

    @Override
    public Path getUploadDir() {
        return this.uploadDir;
    }

    @Override
    public Resource load(String filename) {
        return null;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }
}
