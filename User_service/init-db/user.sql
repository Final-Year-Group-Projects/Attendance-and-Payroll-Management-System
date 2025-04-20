CREATE DATABASE user_db

CREATE TABLE userdata (
    user_id SERIAL PRIMARY KEY,          -- auto-incrementing primary key
    user_name VARCHAR(255) NOT NULL,     -- user name field
    user_address VARCHAR(255),           -- user address field (nullable)
    user_telephone VARCHAR(15),          -- user telephone field
    user_type VARCHAR(50)                -- user type field (could be 'admin', 'user', etc.)
);

INSERT INTO userdata (user_name, user_type, user_address, user_telephone)
VALUES ('Judy', 'User', '123 User St', '123-456-7890')
ON CONFLICT (user_id) DO NOTHING;


SELECT * FROM userdata

DROP TABLE role;

CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    department_head VARCHAR(255)  -- optional: name of head or could link to a user_id
);

ALTER TABLE userdata
ADD COLUMN department_id INTEGER;

ALTER TABLE userdata
ADD COLUMN role_id INTEGER;

CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    role_description VARCHAR(255)  -- optional: name of head or could link to a user_id
);
