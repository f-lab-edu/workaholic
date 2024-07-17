package com.project.workaholic.response.model;

import com.project.workaholic.response.model.enumeration.StatusCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ApiResponse<T> {
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final String reasonPhrase;
    private final String message;
    private final T data;

    @Builder
    public ApiResponse(String reasonPhrase, String message, T data) {
        this.reasonPhrase = reasonPhrase;
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<ApiResponse<StatusCode>> success(StatusCode statusCode) {
        return ResponseEntity
                .status(statusCode.getHttpStatus())
                .body(
                        ApiResponse.<StatusCode>builder()
                                .reasonPhrase(statusCode.getHttpStatus().getReasonPhrase())
                                .message(statusCode.getMessage())
                                .build()
                );
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(StatusCode statusCode, T data) {
        return ResponseEntity
                .status(statusCode.getHttpStatus())
                .body(
                        ApiResponse.<T>builder()
                                .reasonPhrase(statusCode.getHttpStatus().getReasonPhrase())
                                .message(statusCode.getMessage())
                                .data(data)
                                .build()
                );
    }

    public static ResponseEntity<ApiResponse<StatusCode>> error(StatusCode statusCode) {
        return ResponseEntity
                .status(statusCode.getHttpStatus())
                .body(
                        ApiResponse.<StatusCode>builder()
                                .reasonPhrase(statusCode.getHttpStatus().getReasonPhrase())
                                .message(statusCode.getMessage())
                                .build()
                );
    }
}
