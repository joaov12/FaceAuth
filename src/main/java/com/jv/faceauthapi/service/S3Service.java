package com.jv.faceauthapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3client;

    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");

    public void uploadPhoto(MultipartFile photo, boolean isTemp) throws Exception {
        String fileName = isTemp ? photo.getOriginalFilename() + "temporary" : photo.getOriginalFilename();
        File file = new File(Objects.requireNonNull(fileName));
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(photo.getBytes());
        }

        s3client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file));
        file.delete();
    }

    public void deleteTempPhoto(String fileName) {
        s3client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, fileName));
    }
}
