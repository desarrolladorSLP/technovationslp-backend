package org.desarrolladorslp.technovation.exception;

public class UserAlreadyRegisteredInBatch extends RuntimeException {

    public UserAlreadyRegisteredInBatch(String message){
        super(message);
    }
}
