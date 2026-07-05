package com.javatodev.finance.controller;

import com.javatodev.finance.model.dto.response.StatementResponse;
import com.javatodev.finance.service.StatementService;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/statements")
@Validated
public class StatementController {

    private final StatementService statementService;

    // get statements for a given account
    @GetMapping
    public ResponseEntity<Page<StatementResponse>> getStatements(@RequestParam("accountNumber")
                                                                     @NotBlank(message = "Account number cannot be blank")
                                                                     String accountNumber,
                                                                 Pageable pageable) {
        log.info("Got statement request from API {}", accountNumber);
        return ResponseEntity.ok(statementService.readStatements(accountNumber, pageable));
    }
}
