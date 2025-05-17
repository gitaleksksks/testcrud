package com.example.testcrud.repository;

import com.example.testcrud.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByAdditionalInfo(String additionalInfo);

}
