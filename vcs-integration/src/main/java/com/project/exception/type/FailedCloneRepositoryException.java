package com.project.exception.type;

import lombok.Getter;

@Getter
public class FailedCloneRepositoryException extends Exception {
    private final String cloneUrl;

    public FailedCloneRepositoryException(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }
}
