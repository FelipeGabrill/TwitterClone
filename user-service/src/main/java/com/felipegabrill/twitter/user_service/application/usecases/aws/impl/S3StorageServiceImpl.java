package com.felipegabrill.twitter.user_service.application.usecases.aws.impl;

import com.felipegabrill.twitter.user_service.application.exceptions.MediaDeleteException;
import com.felipegabrill.twitter.user_service.application.exceptions.MediaUploadException;
import com.felipegabrill.twitter.user_service.application.usecases.aws.S3UseCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(S3StorageServiceImpl.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3StorageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(MultipartFile file, String folder, String fileName) {
        String key = folder + "/" + fileName;

        log.info("Uploading file to S3 bucket={} key={}", bucketName, key);
        log.debug("File details: contentType={}, size={} bytes",
                file.getContentType(), file.getSize());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            String fileUrl = getFileUrl(key);
            log.info("File uploaded successfully to S3 key={}", key);

            return fileUrl;

        } catch (IOException e) {
            log.error("Error uploading file to S3 key={}", key, e);
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

            log.info("Deleting file from S3 bucket={} key={}", bucketName, key);

            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build()
            );

            log.info("File deleted successfully from S3 key={}", key);

        } catch (Exception e) {
            log.error("Error deleting file from S3 fileUrl={}", fileUrl, e);
            throw new MediaDeleteException("Error deleting file.");
        }
    }

    private String getFileUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}
