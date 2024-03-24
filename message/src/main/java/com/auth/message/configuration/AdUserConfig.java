package com.auth.message.configuration;

import com.auth.message.entity.Role;
import com.auth.message.entity.Users;
import com.auth.message.repository.RepositoryRole;
import com.auth.message.repository.RepositoryUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdUserConfig implements CommandLineRunner {
    private final RepositoryRole repositoryRole;

    private final RepositoryUser repository;

    private final BCryptPasswordEncoder passwordEncoder;

    public AdUserConfig(RepositoryRole repositoryRole, RepositoryUser repository, BCryptPasswordEncoder passwordEncoder) {
        this.repositoryRole = repositoryRole;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = repositoryRole.findByName(Role.Values.ADMIN.name());

        var userAdmin = repository.findByUname("admin");

        userAdmin.ifPresentOrElse(
                users -> {
                    System.out.println("admin ja existe");
                    },
                ()->{
                    var users = new Users();
                    users.setUname("admin");
                    users.setPassword(passwordEncoder.encode("157"));
                    users.setRole(Set.of(roleAdmin));
                    repository.save(users);
                    }
                    );
    }
}
