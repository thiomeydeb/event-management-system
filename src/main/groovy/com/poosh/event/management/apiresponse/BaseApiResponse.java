package com.poosh.event.management.apiresponse;

import java.util.ArrayList;
import java.util.List;

public class BaseApiResponse {
    protected Object data;
    protected int status;
    protected String message;
    protected List<FieldErrorDto> errors = new ArrayList<>();

    public BaseApiResponse() {

    }

    public BaseApiResponse(int status, String message){
        this.status = status;
        this.message = message;
    }

    public BaseApiResponse(Object data, int status, String message, List<FieldErrorDto> errors) {
        this.data = data;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public BaseApiResponse(Object data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldErrorDto> errors) {
        this.errors = errors;
    }
}
