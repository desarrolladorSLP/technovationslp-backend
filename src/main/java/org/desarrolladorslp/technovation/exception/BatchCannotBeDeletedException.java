package org.desarrolladorslp.technovation.exception;

public class BatchCannotBeDeletedException extends RuntimeException {
    private static final long serialVersionUID = 6653160168350326023L;

    public BatchCannotBeDeletedException(String message) {
        super(message);
    }
}
