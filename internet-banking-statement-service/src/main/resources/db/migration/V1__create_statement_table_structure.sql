CREATE TABLE transaction_statement (
    id VARCHAR(36) PRIMARY KEY, -- Menggunakan UUID sebagai Primary Key
    account_number VARCHAR(50) NOT NULL, -- Nomor rekening nasabah
    transaction_date DATETIME NOT NULL, -- Waktu transaksi asli terjadi
    transaction_type VARCHAR(10) NOT NULL, -- CREDIT (Uang masuk) atau DEBIT (Uang keluar)
    amount DECIMAL(19, 4) NOT NULL, -- Jumlah uang
    transaction_reference VARCHAR(100) NOT NULL, -- ID Transaksi dari Core/Fund Transfer (sebagai referensi)
    source_service VARCHAR(50) NOT NULL, -- Misal: "FUND_TRANSFER" atau "UTILITY_PAYMENT"
    description VARCHAR(255), -- Narasi mutasi (Misal: "Transfer ke Ani", "Bayar Listrik")
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Waktu data ini disimpan oleh RabbitMQ Listener

    -- --- [ INDEXING OPTIMIZATION ] ---
    -- Index ini sangat krusial di CQRS karena API kita akan selalu
    -- mencari berdasarkan Nomor Rekening dan mengurutkannya berdasarkan Tanggal.
    INDEX idx_account_date (account_number, transaction_date DESC),

    -- Index opsional jika butuh mencari berdasarkan ID Transaksi asli
    INDEX idx_transaction_ref (transaction_reference)
);