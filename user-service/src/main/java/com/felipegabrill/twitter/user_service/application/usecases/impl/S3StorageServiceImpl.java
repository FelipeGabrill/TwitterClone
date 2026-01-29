package com.felipegabrill.twitter.user_service.application.usecases.impl;

import com.felipegabrill.twitter.user_service.application.exceptions.MediaDeleteException;
import com.felipegabrill.twitter.user_service.application.exceptions.MediaUploadException;
import com.felipegabrill.twitter.user_service.application.usecases.S3UseCases;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;

@Service
public class S3StorageServiceImpl implements S3UseCases {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3StorageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(MultipartFile file, String folder, String fileName) {
        try {
            String key = folder + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return getFileUrl(key);

        } catch (IOException e) {
            throw new MediaUploadException("Error uploading file.");
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            URI uri = new URI(fileUrl);
            String key = uri.getPath();
            if (key.startsWith("/")) {
                key = key.substring(1);
            }

            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build());
        } catch (Exception e) {
            throw new MediaDeleteException("Error deleting file.");
        }
    }

    private String getFileUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}