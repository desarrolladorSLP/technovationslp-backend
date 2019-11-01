package org.desarrolladorslp.technovation.exception;

public class UserAlreadyRegisteredInBatch extends RuntimeException {

    private static final long serialVersionUID = -6832022363329139457L;

    public UserAlreadyRegisteredInBatch(String message) {
        super(message);
    }
}
