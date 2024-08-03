package com.jv.faceauthapi.controller;

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
@RequestMapping("register")
@Tag(name="Registro")
public class RegisterController {
    @Autowired
    private FaceAuthService faceAuthService;

    @Operation(summary = "Upload para o s3", description = "Faz o upload de uma imagem para um bucket do Amazon s3")
    @PostMapping(value = "registerUserPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> sendPhoto(@RequestPart(value = "photo") MultipartFile photo) throws Exception{
        faceAuthService.savePhotoinS3(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body("IMAGEM ENVIADA AO BUCKET DO S3 COM SUCESSO!");
    }
}
