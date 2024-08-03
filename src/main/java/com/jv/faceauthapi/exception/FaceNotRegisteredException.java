package com.jv.faceauthapi.exception;

import java.util.Date;

public class FaceNotRegisteredException extends CustomException{
    public FaceNotRegisteredException() {
        super(new ExceptionResponse(
                new Date(),
                "Rosto não registrado",
                "O rosto fornecido não corresponde a nenhum registro"
        ));
    }
}