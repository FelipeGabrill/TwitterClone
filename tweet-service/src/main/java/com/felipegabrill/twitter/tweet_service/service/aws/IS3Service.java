package com.felipegabrill.twitter.tweet_service.service.aws;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IS3Service {
    /**
     * Uploads multiple files to a specified folder in S3 storage and associates them with a tweet.
     *
     * @param files   the list of files to upload
     * @param folder  the target folder in S3
     * @param tweetId the ID of the tweet to associate the files with
     * @return a list of URLs of the uploaded files
     */
    List<String> uploadFiles(List<MultipartFile> files, String folder, UUID tweetId);

    /**
     * Uploads a single file to S3 storage using a specific key.
     *
     * @param file the file to upload
     * @param key  the S3 key to use for the uploaded file
     * @return the URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String key);
}
