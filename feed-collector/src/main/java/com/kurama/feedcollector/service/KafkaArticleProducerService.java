package com.kurama.feedcollector.service;

import com.kurama.feedcollector.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaArticleProducerService {

    private final KafkaTemplate<String, ArticleDto> kafkaTemplate;

    private static final String TOPIC_NAME = "NEW_ARTICLES";

    public void sendNewArticle(ArticleDto articleDto) {
        try {
            CompletableFuture<SendResult<String, ArticleDto>> future =
                kafkaTemplate.send(TOPIC_NAME, articleDto.getGuid(), articleDto);

            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    log.info("Successfully sent article to Kafka: {} with offset: {}",
                            articleDto.getTitle(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send article to Kafka: {}", articleDto.getTitle(), exception);
                }
            });
        } catch (Exception e) {
            log.error("Error sending article to Kafka: {}", articleDto.getTitle(), e);
        }
    }

    public void sendNewArticles(Iterable<ArticleDto> articles) {
        articles.forEach(this::sendNewArticle);
    }
}
