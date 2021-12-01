package com.poosh.event.management.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.apiresponse.FieldErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionContoller {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadRequestException.class)
    public BaseApiResponse handleApiBadRequestException(BadRequestException ex) {
        return new BaseApiResponse(null, HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getErrors());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = InternalServerErrorException.class)
    public BaseApiResponse handleApiBadRequestException(InternalServerErrorException ex) {
        return new BaseApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ex.getErrors());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseApiResponse handleValidationExceptions(

            MethodArgumentNotValidException ex) {
        List<FieldErrorDto> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.add(new FieldErrorDto(fieldName, errorMessage));
        });
        return new BaseApiResponse(null, 400, "Bad Request - 1", errors);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public BaseApiResponse handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        String errorMessage = ex.getName() + " should be of type " + ex.getRequiredType().getSimpleName();
        List<FieldErrorDto> errors = new ArrayList<>();
        errors.add(new FieldErrorDto(ex.getName(), errorMessage));
        return new BaseApiResponse(null, 400, "Bad Request - 2", errors);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public BaseApiResponse handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<FieldErrorDto> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String field = null;
            for (Path.Node node : violation.getPropertyPath()) {
                field = node.getName();
            }
            errors.add(new FieldErrorDto(field, violation.getMessage()));
        }

        return new BaseApiResponse(null, 400, "Bad Request - 3", errors);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    protected BaseApiResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpStatus status, WebRequest request) {
        String error = "Invalid Request Body. Malformed JSON";
        return new BaseApiResponse(null,400,error,null);
    }


    @ExceptionHandler(JsonMappingException.class)
    public BaseApiResponse handleJsonMappingException(JsonMappingException e){
        String error = "Invalid Request Body. Malformed JSON";
        return new BaseApiResponse(null,400,error,null);

    }
}
