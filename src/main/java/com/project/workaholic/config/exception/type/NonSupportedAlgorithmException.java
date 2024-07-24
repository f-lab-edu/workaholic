package com.project.workaholic.config.exception.type;

import lombok.Getter;

@Getter
public class NonSupportedAlgorithmException extends RuntimeException{
    private final String algorithmName;

    public NonSupportedAlgorithmException(String algorithmName) {
        this.algorithmName = algorithmName;
    }
}
