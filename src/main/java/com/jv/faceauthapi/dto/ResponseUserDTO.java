package com.jv.faceauthapi.dto;

public class ResponseUserDTO {

    private String userName;
    private float percentageSimilarity;

    public ResponseUserDTO() {
    }

    private ResponseUserDTO(Builder builder) {
        this.userName = builder.userName;
        this.percentageSimilarity = builder.percentageSimilarity;
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

    public static class Builder {
        private String userName;
        private float percentageSimilarity;

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder percentageSimilarity(float percentageSimilarity) {
            this.percentageSimilarity = percentageSimilarity;
            return this;
        }

        public ResponseUserDTO build() {
            return new ResponseUserDTO(this);
        }
    }

    @Override
    public String toString() {
        return "ResponseUserDTO{" +
                "userName='" + userName + '\'' +
                ", percentageSimilarity=" + percentageSimilarity +
                '}';
    }
}
