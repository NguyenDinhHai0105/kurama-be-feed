package com.kurama.feedcollector.repository;

import com.kurama.feedcollector.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FeedRepository extends JpaRepository<Feed, UUID> {
    Optional<Feed> findByUrl(String url);
}
