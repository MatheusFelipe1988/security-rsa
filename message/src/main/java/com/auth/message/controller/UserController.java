package com.auth.message.controller;

import com.auth.message.controller.dto.UserDTO;
import com.auth.message.repository.RepositoryUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final RepositoryUser repository;


    public UserController(RepositoryUser repository) {
        this.repository = repository;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok().build();
    }
}
