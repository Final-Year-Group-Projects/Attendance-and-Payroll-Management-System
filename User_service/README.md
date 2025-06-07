# User Service

The **User Service** handles all operations related to user management such as creating, retrieving, updating, deleting, and searching user, department and role details.

## Architecture Diagram
![Image](https://github.com/user-attachments/assets/55da1de6-4f58-4f85-845e-bf2e09397500)


---
## API Endpoints

#### Create new user, new department and new role
```
curl -X POST http://localhost:8084/user/create/departments -H "Content-Type: application/json" -H "Authorization: Bearer <access token here>" -d "{\"departmentName\": \"Finance\", \"departmentHead\": \"Judy Kaushalya\"}"
```
```
curl -X POST http://localhost:8084/user/create/roles -H "Content-Type: application/json" -H "Authorization: Bearer <access token here>" -d "{\"roleName\": \"Intern\", \"roleDescription\": \"example description\"}"
```
```
curl -X POST http://localhost:8084/user/create/users -H "Content-Type: application/json" -H "Authorization: Bearer <access token here>" -d "{\"userId\":\"E100\",\"userFullName\":\"Judy Kaushalya\",\"userType\":\"Admin\",\"userAddress\":\"123 Main St, Springfield\",\"userTelephone\":\"1234567890\",\"departmentId\":1,\"roleId\":1}"
```

#### Get all users, departments and roles details
```
curl -X GET http://localhost:8084/user/getAll/users -H "Authorization: Bearer <access token here>"

```
```
curl -X GET http://localhost:8084/user/getAll/departments -H "Authorization: Bearer <access token here>"
```
```
curl -X GET http://localhost:8084/user/getAll/roles -H "Authorization: Bearer <access token here>"
```

#### Get user, department, role details by ID
```
curl -X GET http://localhost:8084/user/get/users/1 -H "Authorization: Bearer <access token here>"
```
```
curl -X GET http://localhost:8084/user/get/departments/1 -H "Authorization: Bearer <access token here>"
```
```
curl -X GET http://localhost:8084/user/get/roles/1 -H "Authorization: Bearer <access token here>"
```

#### Update users, departments and roles by ID
```
curl -X PUT http://localhost:8084/user/update/users/1 -H "Content-Type: application/json" -H "Authorization: Bearer <access token here>" -d "{\"userId\":\"E100\",\"userName\": \"Alice Johnson Updated\", \"userAddress\": \"456 Updated Street\", \"userTelephone\": \"098656543\", \"userType\": \"Admin\"}"
```
```
curl -X PUT http://localhost:8084/user/update/departments/1 -H "Content-Type: application/json" -H "Authorization: Bearer <access token here>" -d "{\"departmentName\": \"Finance\", \"departmentHead\": \"Judy Kaushalya\"}"
```
```
curl -X PUT http://localhost:8084/user/update/roles/1 -H "Content-Type: application/json" -H "Authorization: Bearer <access token here>" -d "{\"roleName\": \"Intern\", \"roleDescription\": \"example description\"}"
```

#### Delete users, departments and roles by ID
```
curl -X DELETE http://localhost:8084/user/delete/users/1 -H "Authorization: Bearer <access token here>"
```
```
curl -X DELETE http://localhost:8084/user/delete/departments/1 -H "Authorization: Bearer <access token here>"
```

```
curl -X DELETE http://localhost:8084/user/delete/roles/1 -H "Authorization: Bearer <access token here>"
```

#### Search users, departments and roles by their name
```
curl -X GET "http://localhost:8084/user/search/users?name=Judy" -H "Authorization: Bearer <access token here>"
```
```
curl -X GET "http://localhost:8084/user/search/departments?name=Finance" -H "Authorization: Bearer <access token here>"
```
```
curl -X GET "http://localhost:8084/user/search/roles?name=Intern" -H "Authorization: Bearer <access token here>"
```


---
### Admin access endpoints
* All endpoints


### Employee access endpoints
* Can Search users, departments and roles by their name
* Can Get users, departments and roles by their ID
* Can Update their own user details (not others)


---
## How to Run

#### stops and deletes all containers defined and create new one
```
docker-compose down -v
```

```
docker-compose up --build
```
Then run above curls


---
You can import the full API request collection into Postman using the link below:

To use:
1. Open Postman
2. Click `Import`
3. Choose the file `user_service_endpoints.json`