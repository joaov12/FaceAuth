package com.jv.faceauthapi.exception.handler;

import com.jv.faceauthapi.exception.ExceptionResponse;
import com.jv.faceauthapi.exception.FaceNotRegisteredException;
import com.jv.faceauthapi.exception.MultipleFacesDetectedException;
import com.jv.faceauthapi.exception.NoFaceDetectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class AllExceptionsHandler {


    @ExceptionHandler(NoFaceDetectedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleNoFaceDetectedException(NoFaceDetectedException ex) {
        return buildResponseEntity(ex.getExceptionResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultipleFacesDetectedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleMultipleFacesDetectedException(MultipleFacesDetectedException ex) {
        return buildResponseEntity(ex.getExceptionResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FaceNotRegisteredException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleFaceNotRegisteredException(FaceNotRegisteredException ex) {
        return buildResponseEntity(ex.getExceptionResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                "Erro interno do servidor",
                "Um erro inesperado ocorreu. Tente novamente mais tarde."
        );
        return buildResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(ExceptionResponse response, HttpStatus status) {
        return new ResponseEntity<>(response, status);
    }
}
