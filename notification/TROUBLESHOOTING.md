# Kafka Consumer Troubleshooting Guide

## Issue: JsonDeserializer Error

If you see an error like:
```
Value Deserializers with error: Deserializers{keyDeserializer=org.apache.kafka.common.serialization.StringDeserializer@..., valueDeserializer=org.springframework.kafka.support.serializer.JsonDeserializer@...}
```

This has been resolved with the proper configuration.

## Solution Applied

### 1. **KafkaConsumerConfig.java**
Created a proper Kafka consumer configuration with:
- `ErrorHandlingDeserializer` wrapper around `JsonDeserializer`
- Explicit JsonDeserializer configuration with trusted packages
- Disabled type headers (matching producer config)
- Set default type to `ArticleMessage.class`

### 2. **application.yml**
Configured Spring Kafka properties:
- Set trusted packages to `*`
- Disabled type headers usage
- Specified the default type for deserialization

### 3. **ArticleMessage DTO**
Created a DTO that matches the exact structure of the Kafka message:
```json
{
  "guid": "...",
  "title": "...",
  "link": "...",
  "description": "...",
  "author": "...",
  "publishDate": 1754442000000,
  "content": null,
  "category": "...",
  "feedId": "..."
}
```

## Testing the Consumer

1. **Start Kafka** (if not running):
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh config/server.properties
```

2. **Start the Notification Service**:
```bash
./mvnw spring-boot:run
```

3. **Verify Consumer is Listening**:
Check the logs for:
```
INFO ... - partitions assigned: [NEW_ARTICLES-0]
```

4. **Test with Sample Message**:
Use the feed-collector service to publish a message, or manually publish:
```bash
kafka-console-producer --broker-list localhost:9092 --topic NEW_ARTICLES --property "parse.key=true" --property "key.separator=:"

# Then enter:
https://example.com:{"guid":"https://example.com","title":"Test Article","link":"https://example.com","description":"Test description","author":"Test Author","publishDate":1754442000000,"content":null,"category":"Test","feedId":"test-123"}
```

## Expected Logs

When a message is consumed successfully, you should see:
```
INFO ... - Received message from topic: NEW_ARTICLES, partition: 0, offset: X, key: ...
INFO ... - Processing article: 'Meet your new AI coding teammate: Gemini CLI GitHub Actions'
INFO ... - Article details:
INFO ... -   - GUID: https://blog.google/technology/developers/introducing-gemini-cli-github-actions/
INFO ... -   - Link: https://blog.google/technology/developers/introducing-gemini-cli-github-actions/
INFO ... -   - Author: 
INFO ... -   - Category: Developers
INFO ... -   - FeedId: 1fe2dc63-cf2e-46d5-a61c-99144319ecc6
INFO ... -   - Publish Date: 1754442000000
INFO ... -   - Description: <img src="...">...
DEBUG ... - Business logic processing for article: Meet your new AI coding teammate: Gemini CLI GitHub Actions
INFO ... - Successfully processed article: Meet your new AI coding teammate: Gemini CLI GitHub Actions
```

## Common Issues and Solutions

### Issue: "Trusted packages" error
**Solution**: Already configured with `*` in both code and YAML

### Issue: Type header mismatch
**Solution**: Disabled type headers to match producer configuration

### Issue: Class not found during deserialization
**Solution**: Specified `VALUE_DEFAULT_TYPE` to `ArticleMessage.class`

### Issue: Consumer not starting
**Check**:
1. Kafka is running on `localhost:9092`
2. Topic `NEW_ARTICLES` exists
3. No firewall blocking port 9092

### Issue: Messages not being consumed
**Check**:
1. Consumer group ID is correct: `notification-service`
2. Check offset: May need to reset if testing
```bash
kafka-consumer-groups --bootstrap-server localhost:9092 --group notification-service --reset-offsets --to-earliest --topic NEW_ARTICLES --execute
```

## Architecture

```
Feed Collector (Producer)
        ↓
    Kafka Topic: NEW_ARTICLES
        ↓
Notification Service (Consumer)
        ↓
    Business Logic (processArticle)
```

## Dependencies Required

Make sure your `pom.xml` includes:
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

