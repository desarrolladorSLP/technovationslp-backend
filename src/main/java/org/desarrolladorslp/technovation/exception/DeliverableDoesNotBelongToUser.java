package org.desarrolladorslp.technovation.exception;

public class DeliverableDoesNotBelongToUser extends RuntimeException {
    public DeliverableDoesNotBelongToUser(String message) {
        super(message);
    }
}
