package com.felipegabrill.twitter.user_service.application.usecases.aws;

import org.springframework.web.multipart.MultipartFile;

public interface S3UseCases {

    /**
     * Uploads a file to a specified folder in S3 storage.
     *
     * @param file     the file to upload
     * @param folder   the target folder in S3
     * @param fileName the name to assign to the uploaded file
     * @return the URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String folder, String fileName);

    /**
     * Deletes a file from S3 storage.
     *
     * @param fileUrl the URL of the file to delete
     */
    void deleteFile(String fileUrl);
}
