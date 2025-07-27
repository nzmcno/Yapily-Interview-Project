-- BankLite Database Initialization Script
-- This script runs automatically when PostgreSQL container starts

-- Create the banklite user if not exists
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'banklite_user') THEN
      CREATE USER banklite_user WITH ENCRYPTED PASSWORD 'banklite_password';
   END IF;
END
$$;

-- Grant privileges to banklite_user
GRANT ALL PRIVILEGES ON DATABASE banklite TO banklite_user;
GRANT ALL ON SCHEMA public TO banklite_user;

-- Connect to banklite database
\c banklite;

-- Grant additional privileges after connecting to the database
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO banklite_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO banklite_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO banklite_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO banklite_user;

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    account_holder_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL CHECK (currency IN ('USD', 'EUR', 'GBP')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_accounts_account_number ON accounts(account_number);
CREATE INDEX IF NOT EXISTS idx_accounts_currency ON accounts(currency);
CREATE INDEX IF NOT EXISTS idx_accounts_created_at ON accounts(created_at);

-- Create trigger function for auto-updating updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger for accounts table
DROP TRIGGER IF EXISTS update_accounts_updated_at ON accounts;
CREATE TRIGGER update_accounts_updated_at 
    BEFORE UPDATE ON accounts 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Insert sample data for testing
INSERT INTO accounts (account_holder_name, account_number, balance, currency) VALUES
('John Doe', 'ACC1698765432001', 1500.00, 'USD'),
('Jane Smith', 'ACC1698765432002', 2750.50, 'EUR'),
('Bob Johnson', 'ACC1698765432003', 850.25, 'GBP'),
('Alice Brown', 'ACC1698765432004', 3200.00, 'USD'),
('Charlie Wilson', 'ACC1698765432005', 975.75, 'EUR')
ON CONFLICT (account_number) DO NOTHING;

-- Grant permissions on the created table to banklite_user
GRANT ALL PRIVILEGES ON TABLE accounts TO banklite_user;
GRANT ALL PRIVILEGES ON SEQUENCE accounts_id_seq TO banklite_user;

-- Verify setup
SELECT 'Database setup completed successfully!' as status;
SELECT COUNT(*) as sample_accounts_created FROM accounts;