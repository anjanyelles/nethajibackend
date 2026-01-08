package com.nethaji.repositories;

import com.nethaji.entity.UserHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserHierarchyRepository extends JpaRepository<UserHierarchy, UUID> {

    List<UserHierarchy> findBySupervisorIdAndHierarchyType(UUID principalId, UserHierarchy.HierarchyType hierarchyType);


    Optional<UserHierarchy> findBySupervisorIdAndSubordinateId(UUID lectureId, UUID id);
}
