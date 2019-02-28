package org.desarrolladorslp.technovation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InactiveUserException extends RuntimeException {

    private static final long serialVersionUID = 8730149161598193908L;

    public InactiveUserException(String message) {
        super(message);
    }
}
