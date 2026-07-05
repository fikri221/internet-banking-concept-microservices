package com.javatodev.finance.exception;
import lombok.Getter;

@Getter
public class SimpleBankingGlobalException extends RuntimeException {
    private final String code;

    public SimpleBankingGlobalException(String code, String message) {
        super(message); // Mengisi message ke parent (RuntimeException)
        this.code = code;
    }
}
