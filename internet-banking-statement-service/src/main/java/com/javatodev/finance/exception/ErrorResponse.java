package com.javatodev.finance.exception;

public record ErrorResponse (
     String code,
     String message
) {}
