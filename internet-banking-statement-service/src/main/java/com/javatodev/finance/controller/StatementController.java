package com.javatodev.finance.controller;

import com.javatodev.finance.model.dto.Statement;
import com.javatodev.finance.model.dto.request.StatementRequest;
import com.javatodev.finance.model.dto.response.StatementResponse;
import com.javatodev.finance.service.StatementService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/statements")
public class StatementController {

    private final StatementService statementService;

    // get statements for a given account
    @GetMapping
    public ResponseEntity<List<Statement>> getStatements(@RequestParam("accountNumber") String accountNumber,
                                                         Pageable pageable) {
        log.info("Got fund transfer request from API {}", accountNumber);
        return ResponseEntity.ok(statementService.readStatements(accountNumber, pageable));
    }
}
