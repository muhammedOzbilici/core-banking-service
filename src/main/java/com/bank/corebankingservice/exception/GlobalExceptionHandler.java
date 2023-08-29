package com.bank.corebankingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return new ResponseEntity<>(accountNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

}
