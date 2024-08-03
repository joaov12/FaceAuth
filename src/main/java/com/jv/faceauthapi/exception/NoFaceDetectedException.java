package com.jv.faceauthapi.exception;

import java.util.Date;

public class NoFaceDetectedException extends CustomException{
    public NoFaceDetectedException() {
        super(new ExceptionResponse(
                new Date(),
                "Nenhum rosto detectado",
                "A imagem fornecida não tem um rosto"
        ));
    }
}
