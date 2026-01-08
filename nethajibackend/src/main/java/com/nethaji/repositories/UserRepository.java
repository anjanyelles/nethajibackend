package com.nethaji.repositories;

import com.nethaji.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


    Optional<User> findByEmail(String email);

    Optional<User> findByMobileNumber(String mobileNumber);


    @Query(value = "SELECT enrollment_number FROM users " +
            "WHERE enrollment_number LIKE :prefix || '%' " +
            "ORDER BY created_at DESC LIMIT 1",
            nativeQuery = true)
    String findLastEnrollmentNumberByPrefix(@Param("prefix") String prefix);


    @Query("SELECT u FROM User u WHERE u.userType = :userType")
    List<User> findUsersByType(@Param("userType") User.UserType userType);



    @Query("SELECT u FROM User u WHERE u.userType = :userType")
    Page<User> findUsersByType1(User.UserType userType, Pageable pageable);

    Optional<User> findByEnrollmentNumber(String enrollmentNumber);

    Optional<Object> findByUserType(User.UserType lecturer);

    Optional<User> findByEmailOrMobileNumber(String email, String mobileNumber);


//    Optional<User> findByDepartmentIdAndUserType(UUID departmentId, User.UserType userType);
}
