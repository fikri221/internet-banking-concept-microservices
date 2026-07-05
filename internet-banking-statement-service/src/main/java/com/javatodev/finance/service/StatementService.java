package com.javatodev.finance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.model.dto.response.StatementResponse;
import com.javatodev.finance.model.entity.StatementEntity;
import com.javatodev.finance.model.mapper.StatementMapper;
import com.javatodev.finance.model.repository.StatementRepository;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatementService {

    private final StatementRepository statementRepository;

    private final StatementMapper mapper = new StatementMapper();

    @Getter
    private final ObjectMapper objectMapper;

    /**
     * Read statements for a given account
     * @param accountNumber Account number
     * @param pageable Pageable object
     * @return List of statements
     */
    public Page<StatementResponse> readStatements(String accountNumber, Pageable pageable) {
        log.info("Read statements from {}", accountNumber);

        // Ensure there is a default sort if none was provided in the request
        Pageable pageableWithSort = pageable.getSort().isUnsorted()
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("transactionDate").descending())
                : pageable;

        Page<StatementEntity> statementPage = statementRepository.
                findByAccountNumberOrderByTransactionDateDesc(accountNumber, pageableWithSort);

        log.info("Found {} statements for account {}", statementPage.getTotalElements(), accountNumber);
        return mapper.convertToDtoPage(statementPage, pageableWithSort);
    }
}
