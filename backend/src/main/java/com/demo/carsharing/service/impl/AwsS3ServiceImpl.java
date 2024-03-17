package com.demo.carsharing.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.demo.carsharing.config.AwsClientS3Config;
import com.demo.carsharing.service.AwsS3Service;
import com.demo.carsharing.util.Constants;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

    private final AmazonS3 amazonS3;
    private final AwsClientS3Config clientS3Config;

    @Override
    public String getUrl(String bucketName, String keyName) {
        log.debug("Try get Url of Car image by bucketName {} and keyName {} from S3 bucket",
                bucketName, keyName);
        Region region = Region.of(clientS3Config.getRegion());
        S3Presigner s3Presigner = S3Presigner
                .builder()
                .region(region)
                .build();
        String presignedUrl = getPresignedUrl(s3Presigner, bucketName, keyName);
        s3Presigner.close();
        log.debug("Url of Car image was successfully got from S3 bucket "
                + " by bucketName {} and keyName {}", bucketName, keyName);
        return presignedUrl;
    }

    private String getPresignedUrl(S3Presigner s3Presigner, String bucketName, String keyName) {
        log.debug("Try get PresignedURl of Car image from S3 bucket");
        String presidnetUrl = null;
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(Constants.TIME_TO_LIVE_PRESIGNED_URL))
                    .getObjectRequest(getObjectRequest)
                    .build();
            PresignedGetObjectRequest presignedGetObjectRequest =
                    s3Presigner.presignGetObject(getObjectPresignRequest);
            presidnetUrl = presignedGetObjectRequest.url().toString();
            HttpURLConnection connection = (HttpURLConnection) presignedGetObjectRequest
                    .url().openConnection();
            presignedGetObjectRequest.httpRequest().headers().forEach((header, values)
                    -> values.forEach(value -> connection.addRequestProperty(header, value)));
        } catch (S3Exception | IOException exception) {
            exception.getStackTrace();
        }
        log.debug("PresignedURl of Car image was successfully got from S3 bucket");
        return presidnetUrl;
    }

    @Override
    public String upload(MultipartFile file) {
        log.debug("Try upload Car image S3 bucket");
        File localFile = convertMultipartFileToFile(file);
        amazonS3.putObject(new PutObjectRequest(clientS3Config.getBucketName(),
                file.getOriginalFilename(), localFile));
        log.debug("Car image was successfully upload to S3 bucket");
        return file.getOriginalFilename();
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), convertedFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return convertedFile;
    }
}
