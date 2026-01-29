package com.felipegabrill.twitter.user_service.application.usecases;

import org.springframework.web.multipart.MultipartFile;

public interface S3UseCases {

    String uploadFile(MultipartFile file, String folder, String fileName);

    void deleteFile(String fileUrl);

}
