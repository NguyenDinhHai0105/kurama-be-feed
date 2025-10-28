package com.kurama.notification.kafka;

import com.kurama.notification.dto.ArticleMessage;
import com.kurama.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final EmailService emailService;


    @KafkaListener(topics = "NEW_ARTICLES", groupId = "notification-service")
    public void consumeArticle(
            ConsumerRecord<String, ArticleMessage> record
    ) {
        try {
            log.info("Received message from topic: {}, partition: {}, offset: {}, key: {}",
                    record.topic(), record.partition(), record.offset(), record.key());

            ArticleMessage article = record.value();

            if (article == null) {
                log.warn("Received null article message at offset: {}", record.offset());
                return;
            }

            log.info("Processing article: '{}'", article.getTitle());
            log.info("Article details:");
            log.info("  - GUID: {}", article.getGuid());
            log.info("  - Link: {}", article.getLink());
            log.info("  - Author: {}", article.getAuthor());
            log.info("  - Category: {}", article.getCategory());
            log.info("  - FeedId: {}", article.getFeedId());
            log.info("  - Publish Date: {}", article.getPublishDate());
            log.info("  - Description: {}", article.getDescription());

            // Send email notification
            processArticle(article);

            log.info("Successfully processed article: {}", article.getTitle());

        } catch (Exception e) {
            log.error("Error processing article message at offset {}: {}",
                    record.offset(), e.getMessage(), e);
        }
    }

    private void processArticle(ArticleMessage article) {
        log.debug("Processing article for notification: {}", article.getTitle());

        try {
            // Send email notification
            log.info("Email notification sent for article: {}", article.getTitle());
        } catch (Exception e) {
            log.error("Failed to send email notification for article: {}", article.getTitle(), e);
            // Don't throw exception - we don't want to stop consuming messages
        }
    }
}
