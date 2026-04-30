package com.mineexplorer.write.application.usecases;

public class NoGameInProgressException extends RuntimeException {

    public NoGameInProgressException() {
        super("No game in progress");
    }
}
