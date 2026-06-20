package com.javatodev.finance.controller;

import com.javatodev.finance.model.dto.FundTransfer;
import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.response.FundTransferResponse;
import com.javatodev.finance.service.FundTransferService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transfer")
public class FundTransferController {

    private final FundTransferService fundTransferService;

    // send fund transfer to another account
    @PostMapping
    public ResponseEntity<FundTransferResponse> sendFundTransfer(@RequestBody FundTransferRequest fundTransferRequest,
                                                                 @RequestHeader("X-Idempotency-Key") String idempotencyKey) {
        log.info("Got fund transfer request from API {}", fundTransferRequest.toString());
        return ResponseEntity.ok(fundTransferService.fundTransfer(fundTransferRequest, idempotencyKey));
    }

    // read all fund transfers
    @GetMapping
    public ResponseEntity<List<FundTransfer>> readFundTransfers(Pageable pageable) {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(fundTransferService.readAllTransfers(pageable));
    }
}
