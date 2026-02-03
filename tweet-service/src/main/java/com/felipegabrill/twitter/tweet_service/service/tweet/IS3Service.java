package com.felipegabrill.twitter.tweet_service.service.tweet;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IS3Service {
    List<String> uploadFiles(List<MultipartFile> files, String folder, UUID tweetId);

    String uploadFile(MultipartFile file, String key);
}
