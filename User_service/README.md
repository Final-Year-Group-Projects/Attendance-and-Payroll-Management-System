# User Service

#### Get all users detals
```
curl -X GET http://localhost:8080/users
```

### Get user details by ID
```
curl -X GET http://localhost:8080/users/1
```

### Create new user
```
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d "{\"userName\": \"Alice Johnson\", \"userType\": \"admin\", \"userAddress\": \"123 Main Street\", \"userTelephone\": \"123-456-7890\"}"
```

### Update users by user ID
```
curl -X PUT http://localhost:8080/users/1 -H "Content-Type: application/json" -d "{\"userName\": \"Alice Johnson Updated\", \"userAddress\": \"456 Updated Street\", \"userTelephone\": \"987-654-3210\", \"userType\": \"admin\"}"
```

### Delete user by user ID
```
curl -X DELETE http://localhost:8080/users/1
```

### Search users by their name
```
curl -X GET "http://localhost:8080/users/search?name=Judy"
```