package com.kurama.feedcollector.service;

import com.kurama.feedcollector.dto.ArticleDto;
import com.kurama.feedcollector.entity.Article;
import com.kurama.feedcollector.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleDto> getArticlesByFeedId(UUID feedId) {
        List<Article> articles = articleRepository.findByFeedId(feedId);
        if (articles == null) {
            return null;
        }
        return articles.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ArticleDto toDto(Article article) {
        return ArticleDto.builder()
                .title(article.getTitle())
                .link(article.getLink())
                .description(article.getDescription())
                .author(article.getAuthor())
                .publishDate(article.getPublishDate())
                .guid(article.getGuid())
                .content(article.getContent())
                .category(article.getCategory())
                .feedId(article.getFeedId() != null ? article.getFeedId() : null)
                .build();
    }
}

