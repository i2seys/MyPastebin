create database paste_db;
\c paste_db;
CREATE TABLE paste (
    hash TEXT PRIMARY KEY,
    create_time BIGINT NOT NULL,
    paste TEXT NOT NULL
);
CREATE USER paste_db_user WITH PASSWORD 'd7a29';
GRANT ALL PRIVILEGES ON DATABASE paste_db TO paste_db_user;
GRANT ALL ON SCHEMA public TO paste_db_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO paste_db_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO paste_db_user;
GRANT ALL ON TABLE paste TO paste_db_user;

create database hash_db;
\c hash_db;
CREATE TABLE hash (
    hash TEXT PRIMARY KEY
);
CREATE USER hash_db_user WITH PASSWORD 'vcaa21';
GRANT ALL PRIVILEGES ON DATABASE hash_db TO hash_db_user;
GRANT ALL ON SCHEMA public TO hash_db_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO hash_db_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO hash_db_user;
GRANT ALL ON TABLE hash TO hash_db_user;