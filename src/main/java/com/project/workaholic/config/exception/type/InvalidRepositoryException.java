package com.project.workaholic.config.exception.type;

import lombok.Getter;

@Getter
public class InvalidRepositoryException extends RuntimeException {
    private final String repositoryUrl;

    public InvalidRepositoryException(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }
}
