package org.desarrolladorslp.technovation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidUserException extends RuntimeException {

    private static final long serialVersionUID = -1540068576996577691L;

    public InvalidUserException(String message) {
        super(message);
    }
}
