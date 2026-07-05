package com.javatodev.finance.exception;

import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class ServerApiException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public ServerApiException(HttpStatusCode httpStatusCode) {
        super("Server error: " + httpStatusCode);
    }
}
