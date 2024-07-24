package com.project.workaholic.config.exception.type;

import lombok.Getter;

@Getter
public class FailedCreateDirectory extends RuntimeException{
    private final String directoryPath ;

    public FailedCreateDirectory(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
