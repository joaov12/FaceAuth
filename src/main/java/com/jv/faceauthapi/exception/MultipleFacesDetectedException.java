package com.jv.faceauthapi.exception;

import java.util.Date;

public class MultipleFacesDetectedException extends CustomException{
    public MultipleFacesDetectedException(){
        super( new ExceptionResponse(
                new Date(),
                "Múltiplos rostos detectados",
                "A imagem fornecida contém mais de um rosto, mas é esperado apenas um"
        ));
    }
}
