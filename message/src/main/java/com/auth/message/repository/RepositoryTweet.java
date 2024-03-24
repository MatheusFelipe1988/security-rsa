package com.auth.message.repository;

import com.auth.message.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositoryTweet extends JpaRepository<Tweet, Long> {
}
