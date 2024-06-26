package com.auth.message.controller;

import com.auth.message.controller.dto.FeedDTO;
import com.auth.message.controller.dto.FeedItemDTO;
import com.auth.message.controller.dto.TweetDTO;
import com.auth.message.entity.Role;
import com.auth.message.entity.Tweet;
import com.auth.message.repository.RepositoryTweet;
import com.auth.message.repository.RepositoryUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class TweetController {
    private final RepositoryTweet repositoryTweet;
    private final RepositoryUser repository;

    public TweetController(RepositoryTweet repositoryTweet, RepositoryUser repository) {
        this.repositoryTweet = repositoryTweet;
        this.repository = repository;
    }

    @Operation(summary = "método que publica tweets", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK para tweet adicionado"),
            @ApiResponse(responseCode = "500", description = "ERRO caso o tweet for incorreto")
    })
    @PostMapping("/tweet")
    public ResponseEntity<Void> postTweet(@RequestBody TweetDTO tweetDTO, JwtAuthenticationToken token){

        var usuario = repository.findById(UUID.fromString(token.getName()));

        var tweet = new Tweet();
        tweet.setUsers(usuario.get());
        tweet.setContent(tweetDTO.content());

        repositoryTweet.save(tweet);

        return ResponseEntity.ok().build();

    }

    @Operation(summary = "método para apagar tweets", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK para o novo dado adicionado"),
            @ApiResponse(responseCode = "500", description = "ERRO por falha do usuario em implementar um dado")
    })
    @DeleteMapping("/tweet/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetid,
                                            JwtAuthenticationToken token){

        var tweet = repositoryTweet.findById(tweetid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var usuario = repository.findById(UUID.fromString(token.getName()));

        var isAdmin = usuario.get().getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if(isAdmin || tweet.getUsers().getUserid().equals(UUID.fromString(token.getName()))){
            repositoryTweet.deleteById(tweetid);
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "listando tweets adicionado pelos usuarios como admin", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "quando a listagem dos usuarios pelo admin for OK"),
            @ApiResponse(responseCode = "500", description = "ERRO por falha do comando para listar como ADMIN")
    })
    @GetMapping("/feed")
    public ResponseEntity<FeedDTO> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){

        var tweets = repositoryTweet.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC,
                "timestamp"))
                .map(tweet -> new FeedItemDTO(tweet.getTweetid(), tweet.getContent(),
                        tweet.getUsers().getUname()));

        return ResponseEntity.ok(new FeedDTO(tweets.getContent(), page, pageSize
                ,tweets.getTotalPages(),
                tweets.getTotalPages()));
    }
}
