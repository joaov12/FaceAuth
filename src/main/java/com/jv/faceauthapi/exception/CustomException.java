package com.jv.faceauthapi.exception;

public class CustomException extends RuntimeException{
    private ExceptionResponse exceptionResponse;

    public CustomException(ExceptionResponse exceptionResponse) {
        super(exceptionResponse.getMessage());
        this.exceptionResponse = exceptionResponse;
    }

    public ExceptionResponse getExceptionResponse() {
        return exceptionResponse;
    }
}
