package com.kurama.feedcollector.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "feeds")
public class Feed {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String url;

    private String title;

    private String backgroundImg;

    private Instant lastPolledAt;

    public Feed() {
    }

    public Feed(UUID id, String url, String title, Instant lastPolledAt) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.lastPolledAt = lastPolledAt;
    }

    public static Feed of(String url, String title, String backgroundImg) {
        Feed f = new Feed();
        f.setUrl(url);
        f.setTitle(title);
        f.setBackgroundImg(backgroundImg);
        return f;
    }
}
