package com.epam.am.text;

public class RunnerException extends RuntimeException {
    public RunnerException(String message) {
        super(message);
    }

    public RunnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RunnerException(Throwable cause) {
        super(cause);
    }

    public RunnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
