package vcs.integration.exception.type;

import lombok.Getter;

@Getter
public class FailedCloneRepositoryException extends RuntimeException {
    private final String cloneUrl;

    public FailedCloneRepositoryException(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }
}
