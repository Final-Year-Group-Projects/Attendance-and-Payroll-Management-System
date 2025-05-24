# Auth Service

The **Auth Service** is responsible for managing authentication operations such as user registration, login, and secure token handling (e.g., JWT). It acts as a gatekeeper for other services by validating credentials and generating tokens for access control.

## Architecture Diagram
![Image](https://github.com/user-attachments/assets/55da1de6-4f58-4f85-845e-bf2e09397500)

## API Endpoints
#### Register New User (SignUp)
```
curl -X POST http://localhost:8083/auth/register -H "Content-Type: application/json" -d "{\"username\": \"judy\", \"password\": \"123456\", \"role\": \"Admin\"}"
```

#### Login User (SignIn)
```
curl -X POST http://localhost:8083/auth/login -H "Content-Type: application/json" -d "{\"username\": \"judy\", \"password\": \"123456\"}"
```
This will return the **access token**

#### Validate Token
```
curl -X POST http://localhost:8083/auth/validate -H "Content-Type: application/json" -d "{\"token\": \"<access-token-here>\"}"
```

## How to Run

#### stops and deletes all containers defined and create new one
```
docker-compose down -v
```

```
docker-compose up --build
```
Then run above curls