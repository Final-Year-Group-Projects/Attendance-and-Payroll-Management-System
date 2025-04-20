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

ALTER TABLE userdata
ADD COLUMN department_id INTEGER;

ALTER TABLE userdata
ADD COLUMN role_id INTEGER;

SELECT * FROM userdata

DROP TABLE userdata;
