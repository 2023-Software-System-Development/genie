package com.example.genie.common.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}