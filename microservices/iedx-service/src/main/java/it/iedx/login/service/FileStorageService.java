package it.iedx.login.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {

    void init();

    void save(MultipartFile file);

    void save(MultipartFile file, String directory);

    void save(byte[] data, String filename, String directory);

    void overwrite(MultipartFile file, String directory);

    void overwrite(byte[] data, String filename, String directory);

    File getFile(Path physPath);

    Path getUploadDir();

    Resource load(String filename);

    Stream<Path> loadAll();

}
