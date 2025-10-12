package com.kurama.feedcollector.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    private String guid;
    private String title;
    private String link;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String author;
    private Date publishDate;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String category;
    private UUID feedId;

    @CreatedDate
    private Date createdDate;
}

