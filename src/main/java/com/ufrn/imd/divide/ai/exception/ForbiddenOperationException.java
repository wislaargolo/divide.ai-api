package com.ufrn.imd.divide.ai.exception;

public class ForbiddenOperationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "The user is prohibited from performing this operation on a resource he does not own";

    public ForbiddenOperationException() {
        super(DEFAULT_MESSAGE);
    }

    public ForbiddenOperationException(String message) {
        super(message);
    }
}
