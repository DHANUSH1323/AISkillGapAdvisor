package com.skillgap.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillgap.userservice.model.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>{
    Optional<UserEntity> findBykeycloakUserId(String keycloakUserId);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

}
