package com.auth.message.repository;

import com.auth.message.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepositoryUser extends JpaRepository<Users, UUID> {
    Optional<Users> findByUname(String uname);
}
