package com.jv.faceauthapi.controller;

import com.jv.faceauthapi.dto.ResponseUserDTO;
import com.jv.faceauthapi.service.FaceAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("auth")
@Tag(name="Authenticate")
public class FaceAuthController {

    @Autowired
    private FaceAuthService faceAuthService;

    @Operation(summary = "Autenticação facial", description = "Verifica se o rosto da imagem enviada está na base de dados (bucket s3)")
    @PostMapping(value = "/authUserByFace", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseUserDTO> getAuthenticationByFace(@RequestPart(value = "photo") MultipartFile photo) throws Exception {
        return new ResponseEntity<>(faceAuthService.verifyFaceForAuthentication(photo), HttpStatus.OK);
    }
}
