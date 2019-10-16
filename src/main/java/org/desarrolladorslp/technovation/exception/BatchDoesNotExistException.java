package org.desarrolladorslp.technovation.exception;

public class BatchDoesNotExistException extends RuntimeException {
    private static final long serialVersionUID = 6653160168350326026L;

    public BatchDoesNotExistException(String message) {
        super(message);
    }
}
