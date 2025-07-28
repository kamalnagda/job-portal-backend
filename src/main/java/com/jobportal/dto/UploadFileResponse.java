package com.jobportal.dto;

import lombok.Data;

@Data
public class UploadFileResponse {
    private String url;
    private String publicId;

    public UploadFileResponse(String url, String publicId) {
        this.url = url;
        this.publicId = publicId;
    }
}
