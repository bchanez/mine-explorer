package com.mineexplorer.write.application.domain.models;

public class InvalidGameConfigurationException extends RuntimeException {

    public InvalidGameConfigurationException(String message) {
        super(message);
    }
}
