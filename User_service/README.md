# User Service

The **User Service** handles all operations related to user management such as creating, retrieving, updating, deleting, and searching user, department and role details.

## Architecture Diagram
![Image](https://github.com/user-attachments/assets/55da1de6-4f58-4f85-845e-bf2e09397500)

## API Endpoints
#### Get all users, departments and roles details
```
curl -X GET http://localhost:8084/user/getAll/users -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWR5IiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IkFkbWluIn1dLCJpYXQiOjE3NDgwNjE1NjQsImV4cCI6MTc0ODA2NTE2NH0.iY3-xFWdxpHhmA-4_CL7SajqEId1IlDuKfFkGMmTqVI"

```
```
curl -X GET http://localhost:8080/getAll/departments
```
```
curl -X GET http://localhost:8080/getAll/roles
```

#### Get user, department, role details by ID
```
curl -X GET http://localhost:8080/get/users/1
```
```
curl -X GET http://localhost:8080/get/departments/1
```
```
curl -X GET http://localhost:8080/get/roles/1
```

#### Create new user, new department and new role
```
curl -X POST http://localhost:8084/user/create/users -H "Content-Type: application/json" -H "Authorization: Bearer <access token here>" -d "{\"userId\":\"E100\",\"userFullName\":\"Judy Kaushalya\",\"userType\":\"Admin\",\"userAddress\":\"123 Main St, Springfield\",\"userTelephone\":\"1234567890\",\"departmentId\":1,\"roleId\":1}"
```
```
curl -X POST http://localhost:8080/create/departments -H "Content-Type: application/json" -d "{\"departmentName\": \"Finance\", \"departmentHead\": \"Judy Kaushalya\"}"
```
```
curl -X POST http://localhost:8080/create/roles -H "Content-Type: application/json" -d "{\"roleName\": \"Intern\", \"roleDescription\": \"example description\"}"
```

#### Update users, departments and roles by ID
```
curl -X PUT http://localhost:8080/users/1 -H "Content-Type: application/json" -d "{\"userName\": \"Alice Johnson Updated\", \"userAddress\": \"456 Updated Street\", \"userTelephone\": \"987-654-3210\", \"userType\": \"admin\"}"
```
```
curl -X PUT http://localhost:8080/departments/1 -H "Content-Type: application/json" -d "{\"departmentName\": \"Finance\", \"departmentHead\": \"Judy Kaushalya\"}"
```
```
curl -X PUT http://localhost:8080/roles/1 -H "Content-Type: application/json" -d "{\"roleName\": \"Intern\", \"roleDescription\": \"example description\"}"
```

#### Delete users, departments and roles by ID
```
curl -X DELETE http://localhost:8080/delete/users/1
```
```
curl -X DELETE http://localhost:8080/delete/departments/1
```

```
curl -X DELETE http://localhost:8080/delete/roles/1
```


#### Search users, departments and roles by their name
```
curl -X GET "http://localhost:8080/get/users/search?name=Judy"
```
```
curl -X GET "http://localhost:8080/get/departments/search?name=Finance"
```
```
curl -X GET "http://localhost:8080/get/roles/search?name=Intern"
```
