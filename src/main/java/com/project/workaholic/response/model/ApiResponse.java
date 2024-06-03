package com.project.workaholic.response.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {
    private final LocalDateTime time;
    private final String message;
    private final T data;

    @Builder
    public ApiResponse(String message, T data) {
        this.time = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<ApiResponse<String>> success(String message) {
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<String>builder()
                                .message(message)
                                .build()
                );
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok()
                .body(
                    ApiResponse.<T>builder()
                            .data(data)
                            .build()
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<T>builder()
                                .message(message)
                                .data(data)
                                .build()
                );
    }
}
