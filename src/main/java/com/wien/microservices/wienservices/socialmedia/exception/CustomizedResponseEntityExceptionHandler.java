package com.wien.microservices.wienservices.socialmedia.exception;

import com.wien.microservices.wienservices.socialmedia.beans.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleAllException(Exception ex, WebRequest request) throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InputValidationException.class)
    public final ResponseEntity<ErrorMessage> handleInputValidationException(InputValidationException ex, WebRequest request) throws Exception {
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
