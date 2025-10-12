package com.kurama.feedcollector.controller;

import com.kurama.feedcollector.dto.ArticleDto;
import com.kurama.feedcollector.dto.CommonResponseEntity;
import com.kurama.feedcollector.dto.CreateFeedRequest;
import com.kurama.feedcollector.dto.PagedResponse;
import com.kurama.feedcollector.entity.Feed;
import com.kurama.feedcollector.service.ArticleService;
import com.kurama.feedcollector.service.FeedService;
import com.kurama.feedcollector.service.PollFeedService;
import com.kurama.feedcollector.service.exception.DuplicateFeedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feed")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class FeedController {

    private final FeedService service;
    private final ArticleService articleService;
    private final PollFeedService pollFeedService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateFeedRequest request) {
        try {
            Feed saved = service.createFeed(request);
            return ResponseEntity.created(URI.create("/api/v1/feed/" + saved.getId())).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateFeedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping()
    public CommonResponseEntity<List<Feed>> getAllFeeds() {
        List<Feed> result = service.getAll();
        return new CommonResponseEntity<List<Feed>>().success(200, result, "Success");
    }

    @GetMapping("/{feedId}/articles")
    public CommonResponseEntity<PagedResponse<ArticleDto>> getArticlesByFeedId(
            @PathVariable UUID feedId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page
    ) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        int size = 8; // fixed page size as requested
        PagedResponse<ArticleDto> paged = articleService.getArticlesByFeedId(feedId, pageNumber, size);
        return new CommonResponseEntity<PagedResponse<ArticleDto>>().success(200, paged, "Success");
    }

    @PostMapping("/check-new")
    public CommonResponseEntity<String> checkNewArticles() {
//        pollFeedService.checkNewArticles();
        return new CommonResponseEntity<String>().success(200, "Triggered", "Check started");
    }
}