package com.kurama.feedcollector.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    private String guid;
    private String title;
    private String link;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String author;
    private String publishDate;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String category;
    private UUID feedId;
}

