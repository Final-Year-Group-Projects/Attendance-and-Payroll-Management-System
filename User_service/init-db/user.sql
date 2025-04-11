-- This assumes you're already inside the userdb database

CREATE TABLE IF NOT EXISTS userdata (
    userId VARCHAR(191) PRIMARY KEY,
    userName VARCHAR(191) NOT NULL,
    userType VARCHAR(191) NOT NULL,
    userAddress VARCHAR(191) NOT NULL,
    userTelephone VARCHAR(191) NOT NULL
);

INSERT INTO userdata (userId, userName, userType, userAddress, userTelephone)
VALUES ('User1', 'Employee', 'User', '123 User St', '123-456-7890')
ON CONFLICT (userId) DO NOTHING;
