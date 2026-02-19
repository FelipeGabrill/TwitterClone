package com.felipegabrill.twitter.tweet_service.service.aws.impl;

import com.felipegabrill.twitter.tweet_service.service.aws.IS3Service;
import com.felipegabrill.twitter.tweet_service.service.exceptions.MediaUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String folder, UUID tweetId) {
        if (files == null || files.isEmpty()) {
            log.warn("Upload aborted: no files provided for tweetId={}", tweetId);
            throw new MediaUploadException("No files provided for upload.");
        }

        if (files.size() > 4) {
            log.warn("Upload aborted: too many files ({} files) for tweetId={}",
                    files.size(), tweetId);
            throw new MediaUploadException("Cannot upload more than 4 files per tweet.");
        }

        log.info("Starting upload of {} files for tweetId={}", files.size(), tweetId);

        List<String> urls = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String key = folder + "/" + tweetId + "/" + (i + 1) + ".jpg";

            log.debug(
                    "Uploading file index={} for tweetId={} key={} contentType={} size={} bytes",
                    i + 1,
                    tweetId,
                    key,
                    file.getContentType(),
                    file.getSize()
            );

            urls.add(uploadFile(file, key));
        }

        log.info("Finished uploading files for tweetId={} totalFiles={}", tweetId, urls.size());
        return urls;
    }

    @Override
    public String uploadFile(MultipartFile file, String key) {
        try {
            log.debug("Uploading single file to S3 bucket={} key={}", bucketName, key);

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
            log.debug("File uploaded successfully key={}", key);

            return fileUrl;

        } catch (IOException e) {
            log.error("Error uploading file to S3 key={}", key, e);
            throw new MediaUploadException("Error uploading file.");
        }
    }

    private String getFileUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}
