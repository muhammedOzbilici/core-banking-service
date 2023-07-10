package com.bank.corebankingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Direction")
public class InvalidDirectionException extends RuntimeException {

    public InvalidDirectionException(String message) {
        super(message);
    }
}
