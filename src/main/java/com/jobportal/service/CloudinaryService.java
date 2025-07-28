package com.jobportal.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jobportal.dto.UploadFileResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

	
    @Autowired
    private Cloudinary cloudinary;

    //upload method
    @SuppressWarnings("unchecked")
    public UploadFileResponse uploadFile(MultipartFile file, String folder) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folder);
            options.put("resource_type", "auto");

            Map<String, Object> uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);

            String url = uploadedFile.get("secure_url").toString();
            String publicId = uploadedFile.get("public_id").toString();

            return new UploadFileResponse(url, publicId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }
    //delete method
    @SuppressWarnings("unchecked")
    public void deleteFile(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println("Deleted from Cloudinary: " + result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }
}

	