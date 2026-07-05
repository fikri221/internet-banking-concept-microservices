package com.javatodev.finance.model.dto.request;

import java.math.BigDecimal;

public record FundTransferRequest (
     String fromAccount,
     String toAccount,
     BigDecimal amount,
     String authID
) {}
