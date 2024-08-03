package com.jv.faceauthapi.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.jv.faceauthapi.dto.ResponseUserDTO;
import com.jv.faceauthapi.exception.FaceNotRegisteredException;
import com.jv.faceauthapi.exception.MultipleFacesDetectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;
import java.util.Comparator;


@Service
public class RekognitionService {
    @Autowired
    private AmazonRekognition rekognitionclient;

    private static final float MINIMUM_SIMILARITY = 98L;
    private static final String COLLECTION_ID = "photos";
    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");

    public boolean detectFace(byte[] imageBytes) {
        DetectFacesRequest request = new DetectFacesRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(imageBytes)))
                .withAttributes(Attribute.ALL);

        DetectFacesResult result = rekognitionclient.detectFaces(request);

        if (result.getFaceDetails().isEmpty()) {
            return false;
        }

        if (result.getFaceDetails().size() > 1) {
            throw new MultipleFacesDetectedException();
        }

        return result.getFaceDetails().getFirst().getConfidence() > MINIMUM_SIMILARITY;
    }

    public void indexFaceInCollection(MultipartFile photo) throws Exception {
        String photoName = photo.getOriginalFilename();
        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(new Image()
                        .withS3Object(new com.amazonaws.services.rekognition.model.S3Object()
                                .withBucket(BUCKET_NAME)
                                .withName(photoName)))
                .withCollectionId(COLLECTION_ID)
                .withExternalImageId(photoName)
                .withDetectionAttributes("ALL");

        rekognitionclient.indexFaces(indexFacesRequest);
    }

    public ResponseUserDTO searchForMatchingFace(MultipartFile photo) throws Exception {
        SearchFacesByImageResult result = rekognitionclient.searchFacesByImage(new SearchFacesByImageRequest()
                .withCollectionId(COLLECTION_ID)
                .withImage(new Image()
                        .withS3Object(new com.amazonaws.services.rekognition.model.S3Object()
                                .withBucket(BUCKET_NAME)
                                .withName(photo.getOriginalFilename() + "temporary")))
                .withFaceMatchThreshold(MINIMUM_SIMILARITY));

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
