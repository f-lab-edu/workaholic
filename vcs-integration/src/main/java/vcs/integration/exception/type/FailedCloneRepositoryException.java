package vcs.integration.exception.type;

import lombok.Getter;

@Getter
public class FailedCloneRepositoryException extends Exception {
    private final String cloneUrl;

    public FailedCloneRepositoryException(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }
}
