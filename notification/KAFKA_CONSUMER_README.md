# Kafka Consumer for NEW_ARTICLES Topic

## Overview
This Kafka consumer listens to the `NEW_ARTICLES` topic and processes article messages from the feed-collector service.

## Message Format
The consumer expects messages in the following JSON format:

```json
{
  "guid": "https://blog.google/technology/ai/techspert-what-is-vibe-coding/",
  "title": "Ask a Techspert: What is vibe coding?",
  "link": "https://blog.google/technology/ai/techspert-what-is-vibe-coding/",
  "description": "<img src=\"https://storage.googleapis.com/gweb-uniblog-publish-prod/images/Vibecoding_Hero.max-600x600.format-webp.webp\">Learn more about AI and how it's enabling new development tools, like "vibe coding."",
  "author": "",
  "publishDate": 1759766400000,
  "content": null,
  "category": "Ask a Techspert",
  "feedId": "1fe2dc63-cf2e-46d5-a61c-99144319ecc6"
}
```

## Components

### 1. KafkaConsumer
- **Location**: `com.kurama.notification.kafka.KafkaConsumer`
- **Purpose**: Consumes messages from the NEW_ARTICLES topic
- **Group ID**: `notification-service`
- **Features**:
  - Automatic JSON deserialization
  - Detailed logging with partition, offset, and key information
  - Error handling with try-catch blocks
  - Extracts and logs article details

### 2. ArticleMessage DTO
- **Location**: `com.kurama.notification.dto.ArticleMessage`
- **Purpose**: Data Transfer Object for article messages
- **Fields**:
  - `guid` (String): Unique identifier for the article
  - `title` (String): Article title
  - `link` (String): Article URL
  - `description` (String): Article description (may contain HTML)
  - `author` (String): Article author
  - `publishDate` (Long): Publish date in epoch milliseconds
  - `content` (String): Full article content
  - `category` (String): Article category
  - `feedId` (String): UUID of the source feed

### 3. KafkaConsumerConfig
- **Location**: `com.kurama.notification.kafka.KafkaConsumerConfig`
- **Purpose**: Kafka consumer configuration
- **Settings**:
  - Bootstrap servers: `localhost:9092` (default)
  - Auto-offset reset: `earliest`
  - Concurrency: 3 threads
  - Poll timeout: 3000ms
  - Max poll records: 100
  - Auto-commit enabled with 1s interval

## Configuration

### application.yml
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: notification-service
      auto-offset-reset: earliest
```

## Usage

1. **Start Kafka**: Ensure Kafka is running on `localhost:9092`

2. **Start the Application**: Run the Spring Boot application

3. **Messages are consumed automatically**: The `@KafkaListener` annotation automatically starts consuming messages when the application starts

4. **Add Business Logic**: Implement your notification logic in the TODO section of `KafkaConsumer.consumeArticle()` method

## Example Business Logic

You can add various business logic after receiving articles:

```java
// Send email notifications
emailService.sendNotification(article);

// Store in database
articleRepository.save(article);

// Send push notifications
pushNotificationService.notify(article);

// Forward to another service
restTemplate.postForEntity(notificationUrl, article, Void.class);
```

## Logging

The consumer logs the following information:
- Message reception details (partition, offset, key)
- Article title
- Article details (GUID, link, author, category, feedId)
- Errors during message processing

## Error Handling

- Any exceptions during message processing are caught and logged
- The message is acknowledged even if processing fails (auto-commit is enabled)
- Consider implementing a Dead Letter Queue (DLQ) for failed messages in production

## Testing

To test the consumer, you can use the feed-collector service to publish messages, or use a Kafka console producer:

```bash
kafka-console-producer --broker-list localhost:9092 --topic NEW_ARTICLES --property "parse.key=true" --property "key.separator=:"
```

Then enter messages in the format:
```
guid:{"guid":"test-guid","title":"Test Article","link":"http://example.com","description":"Test","author":"Author","publishDate":1759766400000,"content":null,"category":"Test","feedId":"test-feed-id"}
```

## Monitoring

Monitor the consumer lag using Kafka tools:
```bash
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group notification-service
```

