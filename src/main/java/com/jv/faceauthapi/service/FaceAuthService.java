package com.jv.faceauthapi.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;

public class FaceAuthService {

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private AmazonRekognition rekognitionclient;

    private static float similiarity = 98L;
    private static final String COLLECTION_ID = "photos";
    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");

    private void savePhotoinS3(MultipartFile photo) throws Exception {
        if(!isFace(photo.getBytes())){
            throw new Exception("NÃO FOI IDENTIFICADO NENHUM ROSTO!");
        }
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
            throw new Exception("MAIS DE UM ROSTO FOI IDENTIFICADO NA FOTO ENVIADA");
        }

        return result.getFaceDetails().getFirst().getConfidence() > 85;
    }


}
