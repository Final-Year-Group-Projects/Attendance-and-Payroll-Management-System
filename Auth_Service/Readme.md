# Auth Service

The **Auth Service** is responsible for managing authentication operations such as user registration, login, and secure token handling (e.g., JWT). It acts as a gatekeeper for other services by validating credentials and generating tokens for access control.

## Architecture Diagram
![Image](https://github.com/user-attachments/assets/55da1de6-4f58-4f85-845e-bf2e09397500)

## API Endpoints
#### Register New User (SignUp)
```
curl -X GET http://localhost:8080/auth/register -H "Content-Type: application/json" \-d '{"username": "john_doe", "password": "securePassword123", "Role": "Admin"}'
```

#### Login User (SignIn)
```
curl -X GET http://localhost:8080/auth/login -H "Content-Type: application/json" \-d '{"username": "john_doe", "password": "securePassword123"}'
```
This will return the **access token**

#### Validate Token
```
curl -X GET http://localhost:8080/auth/validate -H "Content-Type: application/json" \-d '{"token": "<enter-token-here>""}'
```
