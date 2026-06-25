package com.javatodev.finance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatodev.finance.model.dto.Statement;
import com.javatodev.finance.model.mapper.StatementMapper;
import com.javatodev.finance.model.repository.StatementRepository;

import org.springframework.data.domain.Pageable;
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
    private final ObjectMapper objectMapper;

    /**
     * Read statements for a given account
     * @param accountNumber Account number
     * @param pageable Pageable object
     * @return List of statements
     */
    public List<Statement> readStatements(String accountNumber, Pageable pageable) {
        log.info("Read statements from {}", accountNumber);

        return mapper.convertToDtoList(statementRepository.
                findByAccountNumberOrderByTransactionDateDesc(accountNumber, pageable).getContent());
    }
}
