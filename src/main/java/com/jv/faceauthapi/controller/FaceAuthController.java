package com.jv.faceauthapi.controller;

import com.jv.faceauthapi.dto.ResponseUserDTO;
import com.jv.faceauthapi.service.FaceAuthService;
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
@RequestMapping("Auth")
public class FaceAuthController {

    @Autowired
    private FaceAuthService faceAuthService;

    @PostMapping(value = "sendPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> sendPhoto(@RequestPart(value = "photo")MultipartFile photo) throws Exception{
        faceAuthService.savePhotoinS3(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body("IMAGEM ENVIADA AO BUCKET DO S3 COM SUCESSO!");
    }

    @PostMapping(value = "/byface", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseUserDTO> getAuthenticationByFace(@RequestPart(value = "photo") MultipartFile photo) throws Exception {
        return new ResponseEntity<>(faceAuthService.getAuthenticationByFace(photo), HttpStatus.OK);
    }
}
