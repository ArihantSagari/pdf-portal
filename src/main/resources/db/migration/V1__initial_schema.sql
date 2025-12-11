-- V1__initial_schema.sql
-- Initial schema for ISRO PDF portal (Postgres)

BEGIN;

-- ================================
-- users table
-- ================================
CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  staff_no VARCHAR(64) NOT NULL UNIQUE,
  email VARCHAR(150) UNIQUE,
  full_name VARCHAR(200),
  password VARCHAR(255) NOT NULL,
  department VARCHAR(150),
  designation VARCHAR(150),
  phone VARCHAR(50),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_users_staff_no ON users(staff_no);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- ================================
-- tickets table
-- ================================
CREATE TABLE IF NOT EXISTS tickets (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
  status VARCHAR(30) NOT NULL DEFAULT 'OPEN', -- OPEN, IN_PROGRESS, RESOLVED, CLOSED
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE,
  user_id BIGINT,
  CONSTRAINT fk_tickets_user FOREIGN KEY (user_id)
    REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_tickets_user_id ON tickets(user_id);
CREATE INDEX IF NOT EXISTS idx_tickets_status ON tickets(status);
CREATE INDEX IF NOT EXISTS idx_tickets_priority ON tickets(priority);

-- ================================
-- pdf_records table
-- ================================
CREATE TABLE IF NOT EXISTS pdf_records (
  id BIGSERIAL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  form_type VARCHAR(150),
  form_type_label VARCHAR(255),
  pdf_data BYTEA,
  subject VARCHAR(255),
  user_id BIGINT,
  CONSTRAINT fk_pdf_records_user FOREIGN KEY (user_id)
    REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_pdf_records_user_id_created_at
  ON pdf_records (user_id, created_at DESC);

-- ================================
-- ticket_notifications (simple)
-- ================================
CREATE TABLE IF NOT EXISTS ticket_notifications (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT NOT NULL,
  user_id BIGINT,
  message TEXT,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  read_flag BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT fk_notif_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
  CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_ticket_notifications_user_id ON ticket_notifications(user_id);

COMMIT;
