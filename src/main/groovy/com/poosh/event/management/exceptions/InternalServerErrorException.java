package com.poosh.event.management.exceptions;

import com.poosh.event.management.apiresponse.FieldErrorDto;

import java.util.List;

public class InternalServerErrorException extends RuntimeException {
    private List<FieldErrorDto> errors;

    public InternalServerErrorException(String message, List<FieldErrorDto> errors) {
        super(message);
        this.errors = errors;
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public List<FieldErrorDto> getErrors() {
        return errors;
    }
}
