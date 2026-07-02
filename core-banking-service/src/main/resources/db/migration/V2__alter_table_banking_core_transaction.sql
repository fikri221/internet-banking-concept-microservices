ALTER TABLE banking_core_service.banking_core_transaction MODIFY COLUMN transaction_type enum('FUND_TRANSFER','UTILITY_PAYMENT', 'REVERSAL_UTILITY_PAYMENT') NULL;
