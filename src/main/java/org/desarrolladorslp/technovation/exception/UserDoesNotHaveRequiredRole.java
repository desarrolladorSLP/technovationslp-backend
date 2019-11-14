package org.desarrolladorslp.technovation.exception;

public class UserDoesNotHaveRequiredRole extends RuntimeException {

    private static final long serialVersionUID = -6732112353328139457L;

    public UserDoesNotHaveRequiredRole(String message) {
        super(message);
    }
}
