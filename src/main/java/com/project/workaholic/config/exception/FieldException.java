package com.project.workaholic.config.exception;

import lombok.Getter;

@Getter
public class FieldException {
    private final String field;
    private final String value;
    private final String reason;

    public FieldException(String field, String value, String reason) {
        this.field = field;
        this.value = value;
        this.reason = reason;
    }
}
