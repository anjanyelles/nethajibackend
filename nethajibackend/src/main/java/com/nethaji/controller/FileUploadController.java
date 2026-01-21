package com.nethaji.controller;

import com.nethaji.entity.StaffProfile;
import com.nethaji.entity.StudentProfile;
import com.nethaji.entity.User;
import com.nethaji.entity.UserDocument;
import com.nethaji.repositories.StaffProfileRepo;
import com.nethaji.repositories.StudentProfileRepository;
import com.nethaji.repositories.UserDocumentRepository;
import com.nethaji.repositories.UserRepository;
import com.nethaji.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/nethaji-service/files")
@CrossOrigin
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private StaffProfileRepo staffProfileRepo;

    @Autowired
    private UserDocumentRepository userDocumentRepository;

    @PostMapping(value = "/users/{userId}/profile-photo", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> uploadProfilePhoto(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("message", "User not found"));
            }

            FileStorageService.StoredFile stored = fileStorageService.storeProfilePhoto(userId, file);

            // Persist into profile
            if (user.getUserType() == User.UserType.STUDENT) {
                StudentProfile sp = studentProfileRepository.findByUserId(userId).orElse(null);
                if (sp == null) {
                    sp = new StudentProfile();
                    sp.setUserId(userId);
                    sp.setIsActive(true);
                    sp.setCreatedAt(new Date());
                    sp.setUpdatedAt(new Date());
                }
                sp.setProfilePictureUrl(stored.getPublicUrl());
                sp.setUpdatedAt(new Date());
                studentProfileRepository.save(sp);
            } else {
                StaffProfile prof = staffProfileRepo.findByUserId(userId);
                if (prof == null) {
                    prof = new StaffProfile();
                    prof.setUserId(userId);
                    prof.setFirstName(user.getFirstName() != null ? user.getFirstName() : "");
                    prof.setLastName(user.getLastName() != null ? user.getLastName() : "");
                    prof.setDepartment("NA");
                    prof.setDesignation("NA");
                    prof.setEmployeeSubject("NA");
                    prof.setJoiningDate(java.time.LocalDate.now());
                    prof.setCreatedAt(new Date());
                    prof.setUpdatedAt(new Date());
                }
                prof.setProfilePicture(stored.getPublicUrl());
                prof.setUpdatedAt(new Date());
                staffProfileRepo.save(prof);
            }

            Map<String, Object> res = new HashMap<>();
            res.put("userId", userId);
            res.put("url", stored.getPublicUrl());
            res.put("fileName", stored.getOriginalFileName());
            res.put("contentType", stored.getContentType());
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping(value = "/users/{userId}/documents", consumes = {"multipart/form-data"})
    public ResponseEntity<UserDocument> uploadDocument(
            @PathVariable UUID userId,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(404).build();
            }

            FileStorageService.StoredFile stored = fileStorageService.storeDocument(userId, file);

            UserDocument doc = new UserDocument();
            doc.setUserId(userId);
            doc.setDocumentType(documentType);
            doc.setFileName(stored.getOriginalFileName());
            doc.setMimeType(stored.getContentType());
            doc.setFileUrl(stored.getPublicUrl());
            doc.setIsActive(true);

            return ResponseEntity.ok(userDocumentRepository.save(doc));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/users/{userId}/documents")
    public ResponseEntity<List<UserDocument>> getDocuments(@PathVariable UUID userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(404).body(Collections.emptyList());
        }
        return ResponseEntity.ok(userDocumentRepository.findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(userId));
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID documentId) {
        UserDocument doc = userDocumentRepository.findById(documentId).orElse(null);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }
        doc.setIsActive(false);
        userDocumentRepository.save(doc);
        return ResponseEntity.ok().build();
    }
}
