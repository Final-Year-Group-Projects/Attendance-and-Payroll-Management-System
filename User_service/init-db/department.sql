SELECT * FROM department

DROP TABLE department;

CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    department_head VARCHAR(255)  -- optional: name of head or could link to a user_id
);
