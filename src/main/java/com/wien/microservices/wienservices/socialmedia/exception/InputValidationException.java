package com.wien.microservices.wienservices.socialmedia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InputValidationException extends RuntimeException {
    public InputValidationException(String exMsg) {
        super(exMsg);
    }
}
