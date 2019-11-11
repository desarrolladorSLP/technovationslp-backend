package org.desarrolladorslp.technovation.exception;

public class UserDoesNotHaveRequieredRole extends RuntimeException {

    private static final long serialVersionUID = -6732112353328139457L;

    public UserDoesNotHaveRequieredRole(String message) {
        super(message);
    }
}
