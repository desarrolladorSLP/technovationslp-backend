package org.desarrolladorslp.technovation.config.controller;

import java.util.NoSuchElementException;

import org.desarrolladorslp.technovation.exception.BatchCannotBeDeletedException;
import org.desarrolladorslp.technovation.exception.UserAlreadyConfirmedException;
import org.desarrolladorslp.technovation.exception.UserAlreadyRegisteredInBatch;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Error> handleBadInputException(Exception ex) {
        return new ResponseEntity<>(new Error()
                .exception(ex.getClass().getCanonicalName())
                .message(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserAlreadyConfirmedException.class, BatchCannotBeDeletedException.class, UserAlreadyRegisteredInBatch.class})
    public ResponseEntity<Error> handleUserAlreadyConfirmedException(Exception ex) {
        return new ResponseEntity<>(new Error()
                .exception(ex.getClass().getCanonicalName())
                .message(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NoSuchElementException.class, UsernameNotFoundException.class})
    public ResponseEntity<Error> handleNoSuchElementException(Exception ex) {
        return new ResponseEntity<>(new Error()
                .exception(ex.getClass().getCanonicalName())
                .message(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    public static class Error {
        String message;
        String exception;

        public Error message(String message) {
            this.message = message;
            return this;
        }

        public Error exception(String exception) {
            this.exception = exception;
            return this;
        }
    }
}
