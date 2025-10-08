package com.kurama.feedcollector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurama.feedcollector.entity.Article;
import com.kurama.feedcollector.entity.Feed;
import com.kurama.feedcollector.repository.ArticleRepository;
import com.kurama.feedcollector.repository.FeedRepository;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PollFeedService {

    private final FeedRepository feedRepository;
    private final ObjectMapper mapper;
    private final ArticleRepository articleRepository;

    @Scheduled(fixedDelay = 1000 * 30)
    public void pollFeeds() {
        log.info("Polling all feeds...");
        List<Feed> feeds = feedRepository.findAll();
        for (Feed feed : feeds) {
            log.info("Polling feed with id: {}", feed.getId());
            fetchFeed(feed.getUrl());
        }
    }

    private void fetchFeed(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            try (var inputStream = connection.getInputStream()) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new InputStreamReader(inputStream));
                List<Article> result = feed.getEntries().stream()
                        .map(entry -> Article.builder().
                                title(entry.getTitle()).
                                link(entry.getLink()).
                                description(entry.getDescription() != null ? entry.getDescription().getValue() : null).
                                author(entry.getAuthor()).
                                publishDate(String.valueOf(entry.getPublishedDate())).
                                guid(entry.getUri()).
                                content(entry.getContents() != null && !entry.getContents().isEmpty() ? entry.getContents().get(0).getValue() : null).
                                category(entry.getCategories() != null && !entry.getCategories().isEmpty() ? entry.getCategories().get(0).getName() : null).
                                feedId(Objects.requireNonNull(feedRepository.findByUrl(url).orElse(null)).getId()).
                                build())
                        .toList();
                articleRepository.saveAll(result);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch or parse feed from url: " + url);
            log.error("Failed to fetch or parse feed from url: {}", url, e);
        }
    }
}
