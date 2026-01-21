package com.nethaji.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public StoredFile storeProfilePhoto(UUID userId, MultipartFile file) throws IOException {
        return store(userId, "profile-photos", file);
    }

    public StoredFile storeDocument(UUID userId, MultipartFile file) throws IOException {
        return store(userId, "documents", file);
    }

    private StoredFile store(UUID userId, String folder, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Empty file");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot > -1 && dot < originalName.length() - 1) {
            ext = originalName.substring(dot);
        }

        Path dir = root.resolve(folder).resolve(userId.toString());
        Files.createDirectories(dir);

        String storedName = UUID.randomUUID() + ext;
        Path target = dir.resolve(storedName);
        Files.copy(file.getInputStream(), target);

        String publicPath = "/api/nethaji-service/uploads/" + folder + "/" + userId + "/" + storedName;

        StoredFile out = new StoredFile();
        out.setOriginalFileName(originalName);
        out.setStoredFileName(storedName);
        out.setContentType(file.getContentType());
        out.setAbsolutePath(target.toString());
        out.setPublicUrl(publicPath);
        return out;
    }

    public static class StoredFile {
        private String originalFileName;
        private String storedFileName;
        private String contentType;
        private String absolutePath;
        private String publicUrl;

        public String getOriginalFileName() {
            return originalFileName;
        }

        public void setOriginalFileName(String originalFileName) {
            this.originalFileName = originalFileName;
        }

        public String getStoredFileName() {
            return storedFileName;
        }

        public void setStoredFileName(String storedFileName) {
            this.storedFileName = storedFileName;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getAbsolutePath() {
            return absolutePath;
        }

        public void setAbsolutePath(String absolutePath) {
            this.absolutePath = absolutePath;
        }

        public String getPublicUrl() {
            return publicUrl;
        }

        public void setPublicUrl(String publicUrl) {
            this.publicUrl = publicUrl;
        }
    }
}
