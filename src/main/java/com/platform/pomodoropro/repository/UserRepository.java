package com.platform.pomodoropro.repository;

import com.platform.pomodoropro.entity.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findUserByPhone(String phone);
    UserEntity findUserByEmail(String email);
    Optional<UserEntity> findById(long id);
    UserEntity findUserByEmailOrPhone(String email, String phone);

    @Query(value = "select * from user_entity where role not like 'ADMIN'", nativeQuery = true)
    Page<UserEntity> findNonAdminUsers(PageRequest pageRequest);
}
