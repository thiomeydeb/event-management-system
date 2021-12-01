package com.poosh.event.management.exceptions;

import com.poosh.event.management.apiresponse.FieldErrorDto;

import java.util.List;

public class BadRequestException extends RuntimeException {
    private List<FieldErrorDto> errors;
    public BadRequestException(String message, List<FieldErrorDto> errors) {
        super(message);
        this.errors = errors;
    }

    public List<FieldErrorDto> getErrors() {
        return errors;
    }
}
