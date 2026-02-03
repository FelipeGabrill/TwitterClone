package com.felipegabrill.twitter.tweet_service.service.tweet.impl;

import com.felipegabrill.twitter.tweet_service.service.tweet.IS3Service;
import com.felipegabrill.twitter.tweet_service.service.tweet.exceptions.MediaUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3ServiceImpl implements IS3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String folder, UUID tweetId) {
        if (files == null || files.isEmpty()) {
            throw new MediaUploadException("No files provided for upload.");
        }
        if (files.size() > 4) {
            throw new MediaUploadException("Cannot upload more than 4 files per tweet.");
        }

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String key = folder + "/" + tweetId + "/" + (i + 1) + ".jpg";
            urls.add(uploadFile(file, key));
        }
        return urls;
    }

    @Override
    public String uploadFile(MultipartFile file, String key) {
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

            return getFileUrl(key);

        } catch (IOException e) {
            throw new MediaUploadException("Error uploading file: " + file.getOriginalFilename());
        }
    }

    private String getFileUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}
