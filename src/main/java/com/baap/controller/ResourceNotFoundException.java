package com.baap.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) // This will return a 404 HTTP status when thrown
public class ResourceNotFoundException extends RuntimeException {

    // Constructor that accepts a message to describe the exception
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
