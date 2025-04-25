-- 1. Create the database
CREATE DATABASE auth_db;

-- Connect to auth_db before running the below commands

-- 2. Create the userdata table
CREATE TABLE userdata (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- 3. Insert sample user
INSERT INTO userdata (username, password, role)
VALUES ('Judy', '123456', 'Admin')
ON CONFLICT (username) DO NOTHING;

-- 4. Select all users
SELECT * FROM userdata;
