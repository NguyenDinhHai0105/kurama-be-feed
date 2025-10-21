package com.kurama.feedcollector.repository;

import com.kurama.feedcollector.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {
    List<Article> findByFeedIdOrderByPublishDateDesc(UUID feedId);
    Boolean existsByGuid(String guid);

    // Find articles by multiple GUIDs in one query
    List<Article> findByGuidIn(List<String> guids);

    // Added pageable variant for pagination support
    Page<Article> findByFeedIdOrderByPublishDateDesc(UUID feedId, Pageable pageable);
}
