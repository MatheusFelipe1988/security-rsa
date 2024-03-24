package com.auth.message.repository;

import com.auth.message.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryRole extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
