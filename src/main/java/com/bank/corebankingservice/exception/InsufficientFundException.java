package com.bank.corebankingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Insufficient Fund")
public class InsufficientFundException extends RuntimeException {

    public InsufficientFundException(String message) {
        super(message);
    }
}
