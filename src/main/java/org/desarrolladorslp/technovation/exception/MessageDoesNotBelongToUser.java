package org.desarrolladorslp.technovation.exception;

public class MessageDoesNotBelongToUser extends RuntimeException {
    public MessageDoesNotBelongToUser(String message) {
        super(message);
    }
}
