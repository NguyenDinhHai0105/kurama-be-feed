package com.kurama.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMessage {
    private String guid;
    private String title;
    private String link;
    private String description;
    private String author;
    private Long publishDate;
    private String content;
    private String category;
    private String feedId;
}

