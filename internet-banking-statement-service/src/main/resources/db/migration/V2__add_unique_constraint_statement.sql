ALTER TABLE transaction_statement
    ADD CONSTRAINT uk_statement_idempotency
        UNIQUE (transaction_reference, account_number, transaction_type);