package com.kurama.feedcollector.service;

import com.kurama.feedcollector.dto.CreateFeedRequest;
import com.kurama.feedcollector.entity.Feed;
import com.kurama.feedcollector.repository.FeedRepository;
import com.kurama.feedcollector.service.exception.DuplicateFeedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {

    private final FeedRepository repository;

    public FeedService(FeedRepository repository) {
        this.repository = repository;
    }

    public List<Feed> getAll() {
        return repository.findAll();
    }

    public Feed createFeed(CreateFeedRequest request) {
        if (request == null || request.getUrl() == null || request.getUrl().isBlank()) {
            throw new IllegalArgumentException("url is required");
        }
        // Pre-check uniqueness to return proper 409 instead of generic 500
        if (repository.findByUrl(request.getUrl()).isPresent()) {
            throw new DuplicateFeedException("url already exists");
        }
        try {
            return repository.save(Feed.of(request.getUrl(), request.getTitle(), request.getBackgroundImg()));
        } catch (DataIntegrityViolationException e) {
            // In case of race conditions
            throw new DuplicateFeedException("url already exists", e);
        }
    }
}
