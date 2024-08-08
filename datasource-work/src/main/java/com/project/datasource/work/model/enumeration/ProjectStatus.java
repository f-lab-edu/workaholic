package com.project.datasource.work.model.enumeration;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    CREATE,
    REPO_CLONE,
    BUILD,
    DEPLOY,
    FAILED_CLONE,
    FAILED_DEPLOY,
    FAILED_BUILD,
}
