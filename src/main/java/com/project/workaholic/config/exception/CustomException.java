package com.project.workaholic.config.exception;

import com.project.workaholic.response.model.enumeration.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final StatusCode statusCode;
}
