CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    role_description VARCHAR(255)  -- optional: name of head or could link to a user_id
);

SELECT * FROM role

DROP TABLE role;

