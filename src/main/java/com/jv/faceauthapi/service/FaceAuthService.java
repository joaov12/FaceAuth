package com.jv.faceauthapi.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jv.faceauthapi.dto.ResponseUserDTO;
import com.jv.faceauthapi.exception.FaceNotRegisteredException;
import com.jv.faceauthapi.exception.MultipleFacesDetectedException;
import com.jv.faceauthapi.exception.NoFaceDetectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.Objects;
import java.util.Date;

@Service
public class FaceAuthService {

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private AmazonRekognition rekognitionclient;

    private static float similiarity = 98L;
    private static final String COLLECTION_ID = "photos";
    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");

    public void savePhotoinS3(MultipartFile photo) throws Exception {
        if(!isFace(photo.getBytes())){
            throw new NoFaceDetectedException();
        }

        uploadToBucket(photo, false);



        rekognitionclient.indexFaces(new IndexFacesRequest()
                .withImage(new Image()
                        .withS3Object(new com.amazonaws.services.rekognition.model.S3Object()
                                .withBucket(BUCKET_NAME)
                                .withName(photo.getOriginalFilename())))
                .withCollectionId(COLLECTION_ID)
                .withExternalImageId(photo.getOriginalFilename())
                .withDetectionAttributes("ALL"));
    }

    private void uploadToBucket(MultipartFile photo, boolean isTemp) throws Exception {
        String fileName = isTemp ? photo.getOriginalFilename() + "temp" : photo.getOriginalFilename();

        File file = new File(Objects.requireNonNull(fileName));
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(photo.getBytes());
        }

        s3client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file));
        file.delete();
    }

    private boolean isFace(byte[] bytes) throws Exception {
        DetectFacesRequest request = new DetectFacesRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(bytes)))
                .withAttributes(Attribute.ALL);

        DetectFacesResult result = rekognitionclient.detectFaces(request);

        if(result.getFaceDetails().isEmpty()){
            return false;
        }

        if(result.getFaceDetails().size() > 1){
            throw new MultipleFacesDetectedException();
        }

        return result.getFaceDetails().getFirst().getConfidence() > 85;
    }

    public ResponseUserDTO getAuthenticationByFace(MultipartFile photo) throws Exception {
        if(!isFace(photo.getBytes())){
            throw new NoFaceDetectedException();
        }

        uploadToBucket(photo, true);

        SearchFacesByImageResult result = rekognitionclient.searchFacesByImage(new SearchFacesByImageRequest()
                .withCollectionId(COLLECTION_ID)
                .withImage(new Image()
                        .withS3Object(new com.amazonaws.services.rekognition.model.S3Object()
                                .withBucket(BUCKET_NAME)
                                .withName(photo.getOriginalFilename() + "temp")))
                .withFaceMatchThreshold(similiarity));

        s3client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, photo.getOriginalFilename() + "temp"));

        if (result.getFaceMatches().isEmpty()) {
            throw new FaceNotRegisteredException();
        }

        FaceMatch matchedFace = result.getFaceMatches().stream()
                .max(Comparator.comparing(f -> f.getFace().getConfidence()))
                .orElseThrow(Exception::new);

        return new ResponseUserDTO.Builder()
                .userName(matchedFace.getFace().getExternalImageId())
                .percentageSimilarity(matchedFace.getFace().getConfidence())
                .build();
    }
}