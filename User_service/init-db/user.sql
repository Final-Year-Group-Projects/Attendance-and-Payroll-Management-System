-- 1. Create the user_db database
CREATE DATABASE user_db;

-- 2. Create department table
CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    department_head VARCHAR(255)  -- Can also be a foreign key to userdata if needed
);

-- 3. Create role table
CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    role_description VARCHAR(255)
);

-- 4. Create userdata table with department_id and role_id as foreign keys
CREATE TABLE userdata (
    user_id SERIAL PRIMARY KEY,               -- auto-incrementing primary key
    user_name VARCHAR(255) NOT NULL,          -- user name field
    user_address VARCHAR(255),                -- user address field (nullable)
    user_telephone VARCHAR(15),               -- user telephone field
    user_type VARCHAR(50),                    -- user type field (e.g., 'admin', 'user')
    department_id INTEGER,                    -- department reference
    role_id INTEGER,                          -- role reference
    CONSTRAINT fk_department
        FOREIGN KEY (department_id)
        REFERENCES department(department_id)
        ON DELETE SET NULL,

    CONSTRAINT fk_role
        FOREIGN KEY (role_id)
        REFERENCES role(role_id)
        ON DELETE SET NULL
);

-- Insert sample data into userdata
INSERT INTO userdata (user_name, user_type, user_address, user_telephone)
VALUES ('Judy', 'User', '123 User St', '123-456-7890')
ON CONFLICT (user_id) DO NOTHING;

-- Query all data
SELECT * FROM userdata;

DROP Table department
