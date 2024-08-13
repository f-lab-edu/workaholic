package datasource.work.model.enumeration;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    CREATE,
    CLONING,
    FAILED_CLONE,
    CLONED,
    JIB_INJECTION,
    FAILED_JIB_INJECTION,
    DOCKER_BUILD,
    FAILED_DOCKER_BUILD,
    HUB_PUSH,
    FAILED_HUB_PUSH,
    DEPLOY,
    FAILED_DEPLOY,
}
