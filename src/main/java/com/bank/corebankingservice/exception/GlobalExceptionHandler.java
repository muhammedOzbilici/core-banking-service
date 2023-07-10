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

//    @ExceptionHandler(value = InvalidCurrencyException.class)
//    public ResponseEntity<?> invalidCurrencyException(InvalidCurrencyException invalidCurrencyException) {
//        return new ResponseEntity<>(invalidCurrencyException.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(value = InsufficientFundException.class)
//    public ResponseEntity<?> insufficientFundException(InsufficientFundException insufficientFundException) {
//        return new ResponseEntity<>(insufficientFundException.getMessage(), HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(JsonMappingException.class)
//    public ResponseEntity<?> handleConverterErrors(JsonMappingException exception) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<?> handleConverterErrors(HttpMessageNotReadableException exception) {
//        var message = exception.getMessage() != null &&
//                !exception.getMessage().isEmpty() &&
//                exception.getMessage().contains("com.tuum.bankassignment.entity.Currency") ?
//                "Invalid Currency" : "Bad request";
//        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<?> databaseConnectionFailsException(Exception exception) {
//        logger.info(exception.getMessage());
//        return new ResponseEntity<>("There is a problem", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
