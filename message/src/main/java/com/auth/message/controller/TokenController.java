package com.auth.message.controller;

import com.auth.message.controller.dto.LoginDTO;
import com.auth.message.controller.dto.LoginResponse;
import com.auth.message.entity.Role;
import com.auth.message.repository.RepositoryUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final RepositoryUser repository;

    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, RepositoryUser repository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "realizando login como usuario ou admin", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK quando o login foi realizado"),
            @ApiResponse(responseCode = "403", description = "proiba caso o token inserido for incorreto")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDTO) throws Exception {

        var users = repository.findByUname(loginDTO.uname());

        if(users.isEmpty() || !users.get().isLoginCorrect(loginDTO, passwordEncoder)){
            throw new BadCredentialsException("Usuario invalido");
        }

        var now = Instant.now();

        var expiresIn = 300L;

        var escope = users.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("myJava17")
                .subject(users.get().getUserid().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", escope)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
