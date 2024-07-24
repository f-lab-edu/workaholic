package com.project.workaholic.config.exception.type;

import lombok.Getter;

@Getter
public class FailedCreatedWorkProject extends RuntimeException{
    private final Exception exception;

    public FailedCreatedWorkProject(Exception exception) {
        this.exception = exception;
    }
}
