package com.jv.faceauthapi.dto;

public class ResponseUserDTO {

    private String userName;
    private float percentageSimilarity;

    public ResponseUserDTO() {
    }

    public ResponseUserDTO(float percentageSimilarity, String userName) {
        this.percentageSimilarity = percentageSimilarity;
        this.userName = userName;
    }

    public float getPercentageSimilarity() {
        return percentageSimilarity;
    }

    public void setPercentageSimilarity(float percentageSimilarity) {
        this.percentageSimilarity = percentageSimilarity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
