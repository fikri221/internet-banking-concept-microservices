-- CREATE USER 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS banking_core_service;
CREATE DATABASE IF NOT EXISTS banking_core_fund_transfer_service;
CREATE DATABASE IF NOT EXISTS banking_core_user_service;
CREATE DATABASE IF NOT EXISTS banking_core_utility_payment_service;