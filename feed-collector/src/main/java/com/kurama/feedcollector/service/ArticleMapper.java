package com.kurama.feedcollector.service;

import com.kurama.feedcollector.dto.ArticleDto;
import com.kurama.feedcollector.entity.Article;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleMapper {

    public ArticleDto toDto(Article article) {
        return ArticleDto.builder()
                .guid(article.getGuid())
                .title(article.getTitle())
                .link(article.getLink())
                .description(article.getDescription())
                .author(article.getAuthor())
                .publishDate(article.getPublishDate())
                .content(article.getContent())
                .category(article.getCategory())
                .feedId(article.getFeedId())
                .build();
    }

    public List<ArticleDto> toDtoList(List<Article> articles) {
        return articles.stream()
                .map(this::toDto)
                .toList();
    }
}
