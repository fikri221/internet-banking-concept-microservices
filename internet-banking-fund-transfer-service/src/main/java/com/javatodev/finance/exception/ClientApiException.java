package com.javatodev.finance.exception;

import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.nio.charset.StandardCharsets;

public class ClientApiException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public ClientApiException(HttpStatusCode httpStatusCode, InputStream responseBody) throws IOException {
        super("Client error: " + httpStatusCode + " - " + new String(responseBody.readAllBytes(), StandardCharsets.UTF_8));
    }
}
