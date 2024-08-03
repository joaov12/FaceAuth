package com.jv.faceauthapi.service;

import com.jv.faceauthapi.dto.ResponseUserDTO;
import com.jv.faceauthapi.exception.NoFaceDetectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FaceAuthService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private RekognitionService rekognitionService;

    public void savePhotoInS3(MultipartFile photo) throws Exception {
        if (!rekognitionService.detectFace(photo.getBytes())) {
            throw new NoFaceDetectedException();
        }

        s3Service.uploadPhoto(photo, false);
        rekognitionService.indexFaceInCollection(photo);
    }

    public ResponseUserDTO verifyFaceForAuthentication(MultipartFile photo) throws Exception {
        if (!rekognitionService.detectFace(photo.getBytes())) {
            throw new NoFaceDetectedException();
        }

        s3Service.uploadPhoto(photo, true);
        ResponseUserDTO response = rekognitionService.searchForMatchingFace(photo);
        s3Service.deleteTempPhoto(photo.getOriginalFilename() + "temporary");

        return response;
    }
}