package org.desarrolladorslp.technovation.exception;

public class SessionCannotBeDeletedException extends RuntimeException{
    private static final long serialVersionUID = 6653163068350326076L;

    public SessionCannotBeDeletedException(String message) {
        super(message);
    }

}
