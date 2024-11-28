package com.orellana;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Instant;

public class ResponseBuilder {

    public static ApiResponse success(String message, String presignedURI, String fileName) throws JsonProcessingException {
        ResponseObject responseObject = new ResponseObject(200,message,presignedURI,fileName, Instant.now());

        String body = ObjectMapperConfig.getObjectMapper().writeValueAsString(responseObject);
        return new ApiResponse(200, body);
    }

    public static ApiResponse error(String message) throws JsonProcessingException {
        ResponseObject responseObject = new ResponseObject(500,message,null,null, Instant.now());
        String body = ObjectMapperConfig.getObjectMapper().writeValueAsString(responseObject);
        return new ApiResponse(500, body);
    }

    public static ApiResponse badRequest(String message) throws JsonProcessingException {
        ResponseObject responseObject = new ResponseObject(400,message,null,null, Instant.now());
        String body = ObjectMapperConfig.getObjectMapper().writeValueAsString(responseObject);
        return new ApiResponse(400, body);
    }


    public static ApiResponse error(String message, int statusCode) throws JsonProcessingException {
        ResponseObject responseObject = new ResponseObject(statusCode,message,null,null, Instant.now());
        String body =ObjectMapperConfig.getObjectMapper().writeValueAsString(responseObject);
        return new ApiResponse(500, body);
    }

}
