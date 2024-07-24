package com.project.workaholic.config.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ExceptionResponseWithField {
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final String reasonPhrase;
    private final String message;
    private final List<FieldException> data;

    public ExceptionResponseWithField(String reasonPhrase, String message, List<FieldException> data) {
        this.reasonPhrase = reasonPhrase;
        this.message = message;
        this.data = data;
    }
}
