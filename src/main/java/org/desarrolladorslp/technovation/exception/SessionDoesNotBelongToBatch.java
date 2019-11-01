package org.desarrolladorslp.technovation.exception;

public class SessionDoesNotBelongToBatch extends RuntimeException {

    private static final long serialVersionUID = 1243561068350326076L;

    public SessionDoesNotBelongToBatch(String message) {
        super(message);
    }
}
