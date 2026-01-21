package com.nethaji.repositories;

import com.nethaji.entity.UserDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserDocumentRepository extends JpaRepository<UserDocument, UUID> {
    List<UserDocument> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(UUID userId);
}
