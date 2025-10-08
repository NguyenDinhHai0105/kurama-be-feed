package com.kurama.feedcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeedCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedCollectorApplication.class, args);
    }
}
