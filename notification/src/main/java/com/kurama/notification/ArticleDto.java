package com.kurama.notification;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDto {
    private String guid;
    private String title;
    private String link;
    private String description;
    private String author;
    private Date publishDate;
    private String content;
    private String category;
    private UUID feedId;
}

