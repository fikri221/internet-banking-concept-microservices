package com.javatodev.finance.service.rest.client;

import com.javatodev.finance.model.dto.request.FundTransferRequest;
import com.javatodev.finance.model.dto.response.AccountResponse;
import com.javatodev.finance.model.dto.response.FundTransferResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BankingCoreRestClient {

    private final RestClient bankingCoreClient;

    public AccountResponse readAccount(String accountNumber) {
        return bankingCoreClient.get()
                .uri("/account/bank-account/{account_number}", accountNumber)
                .retrieve()
                .body(AccountResponse.class);
    }

    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest) {
        return bankingCoreClient.post()
                .uri("/transaction/fund-transfer")
                .body(fundTransferRequest)
                .retrieve()
                .body(FundTransferResponse.class);
    }
}
