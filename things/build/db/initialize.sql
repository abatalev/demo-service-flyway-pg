CREATE USER things_admin PASSWORD 'qwerty';
CREATE USER things_user PASSWORD 'qwerty';
CREATE USER things_read PASSWORD 'qwerty';

CREATE DATABASE things_db; 

GRANT CONNECT ON DATABASE things_db TO things_admin;
GRANT CONNECT ON DATABASE things_db TO things_user;
GRANT CONNECT ON DATABASE things_db TO things_read;

\c things_db;

CREATE SCHEMA things_schema;

GRANT CREATE ON SCHEMA things_schema TO things_admin;
GRANT USAGE ON SCHEMA things_schema TO things_admin;
GRANT USAGE ON SCHEMA things_schema TO things_user;
GRANT USAGE ON SCHEMA things_schema TO things_read;

ALTER DEFAULT PRIVILEGES
  FOR USER things_admin
  IN SCHEMA things_schema
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO things_user;

ALTER DEFAULT PRIVILEGES
  FOR USER things_admin
  IN SCHEMA things_schema
  GRANT SELECT ON TABLES TO things_read;

