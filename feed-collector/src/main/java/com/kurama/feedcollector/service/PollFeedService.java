package com.kurama.feedcollector.service;

import com.kurama.feedcollector.dto.ArticleDto;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PollFeedService {

    private final FeedRepository feedRepository;

    public Object pollFeeds(UUID feedId) {
        // Logic to poll feeds and update entries
        List<Object> result = new ArrayList<>();
//        feedRepository.findAll().forEach(feed -> {
//            result.add(fetchFeed(feed.getUrl()));
//        });
        result.add(fetchFeed(Objects.requireNonNull(feedRepository.findById(feedId).orElse(null)).getUrl()));
        return result;
    }

    private List<ArticleDto> fetchFeed(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            try (var inputStream = connection.getInputStream()) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new InputStreamReader(inputStream));
                return feed.getEntries().stream()
                        .map(entry -> new ArticleDto().convertToArticleDto(entry))
                        .toList();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch or parse feed from url: " + url);
            log.error("Failed to fetch or parse feed from url: {}", url, e);
        }
        return null;
    }
}
