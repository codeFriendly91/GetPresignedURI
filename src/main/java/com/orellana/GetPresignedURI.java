package com.orellana;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Map;

public class GetPresignedURI implements RequestHandler<Map<String, Object>, ApiResponse> {

    
    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    private static final String BUCKET_NAME = "filetestbucketaws";// Cambia esto por el nombre de tu bucket.
    @Override
    public ApiResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            String body = (String) input.get("body");
            Map bodyMap = ObjectMapperConfig.getObjectMapper().readValue(body, Map.class);
            String objectKey = (String) bodyMap.get("fileName");

            if (objectKey == null || objectKey.isEmpty()) {
                return ResponseBuilder.badRequest("fileName is required");
            }

            String presignedUrl = generatePresignedUrl(objectKey);
            return ResponseBuilder.success("Ok", presignedUrl, objectKey);

        }catch (Exception e){
            try {
                return ResponseBuilder.error(e.getMessage(),500);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private String generatePresignedUrl(String objectKey) {
        ZonedDateTime expiration = ZonedDateTime.now().plusMinutes(10);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(BUCKET_NAME, objectKey)
                        .withMethod(com.amazonaws.HttpMethod.PUT) // Para subir un archivo
                        .withExpiration(java.util.Date.from(expiration.toInstant()));

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
