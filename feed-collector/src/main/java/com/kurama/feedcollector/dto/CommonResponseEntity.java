package com.kurama.feedcollector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseEntity<T> {
    private Integer statusCode;
    private T data;
    private String message;

    public CommonResponseEntity<T> success(Integer statusCode, T data, String message) {
        return CommonResponseEntity.<T>builder()
                .statusCode(statusCode)
                .data(data)
                .message(message)
                .build();
    }
}
