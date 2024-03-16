package com.demo.carsharing.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {

    String getUrl(String bucketName, String keyName);

    String upload(MultipartFile file);
}
