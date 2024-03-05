package com.platform.pomodoropro.repository;

import com.platform.pomodoropro.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername (String username);
    UserEntity findUserByEmail(String email);
    UserEntity findUserByEmailOrUsername(String email, String username);
}
