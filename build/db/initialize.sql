CREATE USER test_admin PASSWORD 'qwerty';
CREATE USER test_user PASSWORD 'qwerty';
CREATE USER test_read PASSWORD 'qwerty';

CREATE DATABASE test_db; 

GRANT CONNECT ON DATABASE test_db TO test_admin;
GRANT CONNECT ON DATABASE test_db TO test_user;
GRANT CONNECT ON DATABASE test_db TO test_read;

\c test_db;

CREATE SCHEMA test_schema;

GRANT CREATE ON SCHEMA test_schema TO test_admin;
GRANT USAGE ON SCHEMA test_schema TO test_admin;
GRANT USAGE ON SCHEMA test_schema TO test_user;
GRANT USAGE ON SCHEMA test_schema TO test_read;

ALTER DEFAULT PRIVILEGES
  FOR USER test_admin
  IN SCHEMA test_schema
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO test_user;

ALTER DEFAULT PRIVILEGES
  FOR USER test_admin
  IN SCHEMA test_schema
  GRANT SELECT ON TABLES TO test_read;

