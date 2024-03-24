package com.auth.message.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tweet")
@Getter
@Setter
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tweetid")
    private Long tweetid;

    @ManyToOne
    @JoinColumn(name = "userid")
    private Users users;

    private String content;

    @CreationTimestamp
    private Instant timestamp;
}
