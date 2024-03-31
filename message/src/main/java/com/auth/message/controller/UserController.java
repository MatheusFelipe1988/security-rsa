package com.auth.message.controller;

import com.auth.message.controller.dto.UserDTO;
import com.auth.message.entity.Role;
import com.auth.message.entity.Users;
import com.auth.message.repository.RepositoryRole;
import com.auth.message.repository.RepositoryUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {
    private final RepositoryUser repository;
    private final RepositoryRole repositoryRole;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(RepositoryUser repository, RepositoryRole repositoryRole, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.repositoryRole = repositoryRole;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Controller que publica o cadastro do usuario", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "quando o dado injetado for OK"),
            @ApiResponse(responseCode = "401", description = "Erro por erro na injeção do dado")
    })
    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody UserDTO userDTO){

        var basicRole = repositoryRole.findByName(Role.Values.BASIC.name());
        var uDTO = repository.findByUname(userDTO.uname());

        if (uDTO.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new Users();
        user.setUname(userDTO.uname());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setRole(Set.of(basicRole));

        repository.save(user);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "listando usuarios chamados pelo admin", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "quando a listagem dos usuarios cadastrados for OK"),
            @ApiResponse(responseCode = "401", description = "quando der erro na hora de listar usuarios")
    })
    @GetMapping("/users")
    @PreAuthorize("HasAuthority('ESCOPE_ADMIN')")
    public ResponseEntity<List<Users>> listUsers(){

        var users = repository.findAll();

        return ResponseEntity.ok(users);
    }
}
