package com.orellana;

import java.time.Instant;

import java.time.Instant;

public record ResponseObject(int statusCode,String message,String presignedURI,String fileName, Instant timeStamp) {
}
