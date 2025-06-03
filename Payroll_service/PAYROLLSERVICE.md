# Payroll Service

The **Payroll Service** is a Spring Boot microservice responsible for managing employee payrolls, generating payslips (HTML and PDF), handling reimbursements, and providing payroll notifications.

---

##  Features

- Create, read, update, delete payroll records 
- Generate payslip in HTML and PDF formats
- Notify employee with latest payroll info
- Submit and manage reimbursement requests
- Integration with User and Attendance services via Feign clients
- Centralized exception handling and validation
- Role-based endpoint segregation (Admin vs User)

---

## Prerequisites
- **Java 17 (or higher)**
- **Spring Boot 3.4.4**
- **Maven 3.6+**
- **PostgreSQL 15**
- **Docker**

---

### .env structure
```
POSTGRES_DB=<your_payroll_db_name>
POSTGRES_USER=postgres
POSTGRES_PASSWORD=<your_password>
```
---

##  API Endpoints
All endpoints require a valid Bearer token for authorization.  

`Base URL: http://localhost/payroll/`

###  Admin Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/payrolls` | Create new payroll |
| GET    | `/payrolls/{payrollId}` | Get payroll by ID |
| PUT    | `/payrolls/{payrollId}` | Update payroll by ID |
| DELETE | `/payrolls/{payrollId}` | Delete payroll by ID |
| DELETE | `/payrolls/employee/{employeeId}` | Delete all payrolls for an employee |
| GET    | `/payrolls/all` | Get all payroll records |
| POST   | `/payrolls/generateAll?month={month}&year={year}` | Bulk payroll generation |
| PUT    | `/payrolls/{payrollId}/status?status={status}` | Update payroll status |
| PUT    | `/reimbursements/{id}/status?status={status}` | Approve or reject reimbursement |
| DELETE | `/reimbursements/{id}` | Delete reimbursement request |

###  User Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/payrolls/employee/{employeeId}` | View own payroll records |
| GET    | `/payrolls/{payrollId}/payslip` | View payslip (HTML) |
| GET    | `/payrolls/{payrollId}/payslip/pdf` | Download payslip as PDF |
| POST   | `/payrolls/{employeeId}/notify` | Request payroll notification |
| POST   | `/reimbursements` | Submit reimbursement |
| GET    | `/reimbursements/employee/{employeeId}` | View own reimbursements |

---

##  Integrations

- **User Service**: fetch user info and roles `USER_SERVICE_URL=http://localhost:8084`
- **Attendance Service**: retrieve attendance data for payroll calculation `ATTENDENCE_SERVICE_URL=http://localhost:8081`

---

## Payroll API cURL Commands to Test

### 1. Create Payroll Record 
```
curl -X POST "http://localhost:8082/payroll/payrolls" -H "Authorization: Bearer <token>" -H "Content-Type: application/json" -d '{"employeeId":"H01","month":4,"year":2024,"workingDays":16,"approvedLeaves":3,"notApprovedLeaves":1,"role":"HR"}'
```

### 2. Bulk Generate Payrolls 
```
curl -X POST "http://localhost:8082/payroll/payrolls/generateAll?month=3&year=2025" -H "Authorization: Bearer <token>"
```

### 3. Send Payroll Notification 
```
curl -X POST "http://localhost:8082/payroll/payrolls/H01/notify" -H "Authorization: Bearer <token>"
```

### 4. Submit Reimbursement Request 
```
curl -X POST "http://localhost:8082/payroll/reimbursements" -H "Authorization: Bearer <token>" -H "Content-Type: application/json" -d '{"employeeId":"H01","type":"MEDICAL","amount":100,"description":"Medicine and Hospital Fee"}'
```

### 5. Get Payroll by ID 
```
curl -X GET "http://localhost:8082/payroll/payrolls/1" -H "Authorization: Bearer <token>"
```

### 6. Get All Payrolls  
```
curl -X GET "http://localhost:8082/payroll/payrolls/all" -H "Authorization: Bearer <token>"
```

### 7. Get Employee Payrolls 
```
curl -X GET "http://localhost:8082/payroll/payrolls/employee/H01" -H "Authorization: Bearer <token>"
```

### 8. View HTML Payslip 
```
curl -X GET "http://localhost:8082/payroll/payrolls/1/payslip" -H "Authorization: Bearer <token>"
```

### 9. Download PDF Payslip 
```
curl -X GET "http://localhost:8082/payroll/payrolls/1/payslip/pdf" -H "Authorization: Bearer <token>" -o payslip.pdf
```

### 10. Get Employee Reimbursements 
```
curl -X GET "http://localhost:8082/payroll/reimbursements/employee/H01" -H "Authorization: Bearer <token>"
```

### 11. Update Payroll Record 
```
curl -X PUT "http://localhost:8082/payroll/payrolls/1" -H "Authorization: Bearer <token>" -H "Content-Type: application/json" -d '{"employeeId":"H01","month":5,"year":2025,"workingDays":17,"approvedLeaves":2,"notApprovedLeaves":1,"role":"HR"}'
```

### 12. Update Payroll Status 
```
curl -X PUT "http://localhost:8082/payroll/payrolls/1/status?status=CANCELLED" -H "Authorization: Bearer <token>"
```

### 13. Update Reimbursement Status 
```
curl -X PUT "http://localhost:8082/payroll/reimbursements/1/status?status=APPROVED" -H "Authorization: Bearer <token>"
```

### 14. Delete Payroll by ID 
```
curl -X DELETE "http://localhost:8082/payroll/payrolls/1" -H "Authorization: Bearer <token>"
```

### 15. Delete All Employee Payrolls
```
curl -X DELETE "http://localhost:8082/payroll/payrolls/employee/H01" -H "Authorization: Bearer <token>"
```

### 16. Delete Reimbursement 
```
curl -X DELETE "http://localhost:8082/payroll/reimbursements/1" -H "Authorization: Bearer <token>"
```
---

## Set Up Payroll Service


### 1. Service Discovery and API Gateway

The **Payroll Service** is designed to operate within a **microservices architecture** using a service discovery tool such as **Eureka** and an API gateway like **NGINX**. This setup enables:

- Seamless integration and communication between multiple services
- Centralized routing

---

### 2. Running as Standalone

If you're testing or developing locally and **do not require the full microservices stack**, you can run the **Payroll Service** in standalone mode. In this case, service discovery should be **disabled**, and you can access the service directly.

### Steps to Run as a Standalone Service:

#### I. Clone the Repository
```bash
   git clone <repository-url>
   cd Payroll_service
```

#### II. Disable Eureka

- In `application.properties` or `application-dev.properties`, add:

```properties
eureka.client.enabled=false 
```

#### III. Build the application via Docker Compose

Before starting the services, ensure that no previously running containers or volumes interfere with your setup.  

Run the following command to stop and remove all containers, networks, and volumes created by this docker-compose project.

```bash
docker-compose down -v
```

To bring up the entire application stack using Docker Compose, run the following command in your terminal:

```bash
docker-compose up -d --build
```

#### IV. Accessing the Service

Once Docker Compose is up:

- Visit: [http://localhost:8082](http://localhost:8082)
- Ensure your service logs show: `Started PayrollServiceApplication` or similar.
- Test service using above given endpoints (Using Postman etc.)

---

### Key Differences Between Full System and Standalone
| Aspect               | Full System                                                      | Standalone                                                           |
|----------------------|------------------------------------------------------------------|----------------------------------------------------------------------|
| Service Discovery    | Through Eureka                                                   | Disabled                                                             |
| API Access           | Via NGINX (port 80) <br/> `ex:http://localhost:80/payroll/payrolls`| Direct (port 8082) <br/> `ex:http://localhost:8082/payroll/payrolls` |
| Dependencies         | Requires Eureka/Nginx                                            | Only PostgreSQL                                                      |
| Scaling              | Horizontal scaling                                               | Single instance                                                      |


