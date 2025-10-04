package com.kurama.feedcollector.dto;

import lombok.Data;

@Data
public class CreateFeedRequest {
    private String url;
    private String title;
    private String backgroundImg;
}
