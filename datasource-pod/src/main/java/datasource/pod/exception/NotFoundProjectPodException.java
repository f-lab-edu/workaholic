package datasource.pod.exception;

import lombok.Getter;

@Getter
public class NotFoundProjectPodException extends RuntimeException {
    private final String projectId;

    public NotFoundProjectPodException(String projectId) {
        this.projectId = projectId;
    }
}
