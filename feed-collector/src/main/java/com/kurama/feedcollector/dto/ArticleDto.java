package com.kurama.feedcollector.dto;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {
    private String title;
    private String link;
    private String description;
    private String author;
    private String publishDate;
    private String guid;
    private String content;
    private String category;

    public ArticleDto convertToArticleDto(SyndEntry entry) {
        String content = null;
        if (entry.getContents() != null && !entry.getContents().isEmpty()) {
            StringBuilder contentBuilder = new StringBuilder();
            for (SyndContent syndContent : entry.getContents()) {
                contentBuilder.append(syndContent.getValue());
            }
            content = contentBuilder.toString();
        } else if (entry.getDescription() != null) {
            content = entry.getDescription().getValue();
        }

        return ArticleDto.builder()
                .title(entry.getTitle())
                .link(entry.getLink())
                .description(entry.getDescription() != null ? entry.getDescription().getValue() : null)
                .author(entry.getAuthor())
                .publishDate(entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : null)
                .guid(entry.getUri())
//                .content(content)
                .category(entry.getCategories() != null && !entry.getCategories().isEmpty() ? entry.getCategories().get(0).getName() : null)
                .build();
    }
}
