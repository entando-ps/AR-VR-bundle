package it.iedx.login.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Arvr.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {


    // file transfer
    private FileTransfer fileTransfer;

    public FileTransfer getFileTransfer() {
        return fileTransfer;
    }

    public void setFileTransfer(FileTransfer fileTransfer) {
        this.fileTransfer = fileTransfer;
    }

    public static class FileTransfer {
        private String uploadDir;

        public String getUploadDir() {
            return uploadDir;
        }

        public void setUploadDir(String uploadDir) {
            this.uploadDir = uploadDir;
        }
    }

}
