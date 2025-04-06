CREATE USER owners_admin PASSWORD 'qwerty';
CREATE USER owners_user PASSWORD 'qwerty';
CREATE USER owners_read PASSWORD 'qwerty';

CREATE DATABASE owners_db; 

GRANT CONNECT ON DATABASE owners_db TO owners_admin;
GRANT CONNECT ON DATABASE owners_db TO owners_user;
GRANT CONNECT ON DATABASE owners_db TO owners_read;

\c owners_db;

CREATE SCHEMA owners_schema;

GRANT CREATE ON SCHEMA owners_schema TO owners_admin;
GRANT USAGE ON SCHEMA owners_schema TO owners_admin;
GRANT USAGE ON SCHEMA owners_schema TO owners_user;
GRANT USAGE ON SCHEMA owners_schema TO owners_read;

ALTER DEFAULT PRIVILEGES
  FOR USER owners_admin
  IN SCHEMA owners_schema
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO owners_user;

ALTER DEFAULT PRIVILEGES
  FOR USER owners_admin
  IN SCHEMA owners_schema
  GRANT SELECT ON TABLES TO owners_read;

