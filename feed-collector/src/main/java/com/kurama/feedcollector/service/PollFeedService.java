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
import java.util.*;
import java.util.stream.Collectors;

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
                List<Article> allArticles = feed.getEntries().stream()
                        .map(entry -> Article.builder().
                                title(entry.getTitle()).
                                link(entry.getLink()).
                                description(entry.getDescription() != null ? entry.getDescription().getValue() : null).
                                author(entry.getAuthor()).
                                publishDate(entry.getPublishedDate()).
                                guid(entry.getUri()).
                                content(entry.getContents() != null && !entry.getContents().isEmpty() ? entry.getContents().get(0).getValue() : null).
                                category(entry.getCategories() != null && !entry.getCategories().isEmpty() ? entry.getCategories().get(0).getName() : null).
                                feedId(Objects.requireNonNull(feedRepository.findByUrl(url).orElse(null)).getId()).
                                build())
                        .toList();

                List<Article> newArticles = filterNewArticles(allArticles);
                if (!newArticles.isEmpty()) {
                    log.info("Found {} new articles for feed: {}", newArticles.size(), url);
                    articleRepository.saveAll(newArticles);
                } else {
                    log.info("No new articles found for feed: {}", url);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch or parse feed from url: " + url);
            log.error("Failed to fetch or parse feed from url: {}", url, e);
        }
    }

    private List<Article> filterNewArticles(List<Article> articles) {
        if (articles.isEmpty()) {
            return articles;
        }

        // Extract all GUIDs from the articles
        List<String> articleGuids = articles.stream()
                .map(Article::getGuid)
                .filter(Objects::nonNull)
                .toList();

        if (articleGuids.isEmpty()) {
            return articles;
        }

        // Query database once to get all existing articles with these GUIDs
        List<Article> existingArticles = articleRepository.findByGuidIn(articleGuids);
        Set<String> existingGuids = existingArticles.stream()
                .map(Article::getGuid)
                .collect(Collectors.toSet());

        // Filter in memory - only keep articles whose GUID is not in the existing set
        return articles.stream()
                .filter(article -> article.getGuid() != null && !existingGuids.contains(article.getGuid()))
                .toList();
    }
}
