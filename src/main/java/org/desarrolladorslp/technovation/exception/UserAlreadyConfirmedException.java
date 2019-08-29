package org.desarrolladorslp.technovation.exception;

public class UserAlreadyConfirmedException extends RuntimeException {

    public UserAlreadyConfirmedException(String message) {
        super(message);
    }
}
