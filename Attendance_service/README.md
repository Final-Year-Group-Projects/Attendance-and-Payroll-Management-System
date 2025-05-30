# Attendance Service

The Attendance Service is a Spring Boot-based application designed to manage employee attendance and leave requests.
It provides a set of RESTful APIs for both admin and user (employee/admin) access,
utilizing PostgreSQL as the database, NGINX as the API gateway, Eureka for service discovery, and Docker for deployment.

## Features
- Record and manage employee check-ins and check-outs.
- Calculate working hours for individual attendance records and total hours for employees.
- Submit, approve, reject, and delete leave requests with status tracking.
- Retrieve attendance counts and leave balances for specific months.
- Fetch all attendance and leave records for employees.

## Technologies
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **API Gateway**: NGINX
- **Service Discovery**: Eureka
- **Deployment**: Docker

## API Endpoints

### Admin Access Endpoints
These endpoints are restricted to admin users only.

- **PUT `/attendance/leaves/{leaveId}/status`**
    - **Description**: Update the status (e.g., APPROVED, REJECTED) of a leave request.
    - **Request Parameters**:
        - `leaveId` (path variable): ID of the leave request.
        - `status` (query param): New status (e.g., "APPROVED").
        - `employeeId` (header): ID of the admin performing the action.
    - **Response**:
        - `200 OK` with updated leave details.
        - `400 Bad Request` for invalid input.
        - `403 Forbidden` if not an admin.
        - `404 Not Found` if leave ID is invalid.

### User Access Endpoints
These endpoints are accessible to both employees and admins.

- **POST `/attendance/{employeeId}`**
    - **Description**: Record both check-in and check-out for an employee.
    - **Request Body**: `AttendanceRequest` (date, checkInTime, checkOutTime).
    - **Response**:
        - `200 OK` with saved attendance record.
        - `400 Bad Request` for invalid input.

- **POST `/attendance/check-in/{employeeId}`**
    - **Description**: Record check-in for an employee.
    - **Request Body**: `CheckInRequest` (date, checkInTime).
    - **Response**:
        - `200 OK` with saved attendance record.
        - `400 Bad Request` for invalid input.

- **PUT `/attendance/check-out/{recordId}`**
    - **Description**: Record check-out for an existing attendance record.
    - **Request Body**: `CheckOutRequest` (checkOutTime).
    - **Response**:
        - `200 OK` with updated attendance record.
        - `400 Bad Request` if check-out time is invalid.
        - `404 Not Found` if record ID is invalid.

- **GET `/attendance/{employeeId}`**
    - **Description**: Retrieve all attendance records for an employee.
    - **Response**:
        - `200 OK` with list of attendance records.
        - `404 Not Found` if no records exist.

- **GET `/attendance/hours/{recordId}`**
    - **Description**: Calculate working hours for a specific attendance record.
    - **Response**:
        - `200 OK` with working hours.
        - `400 Bad Request` if record is incomplete.
        - `404 Not Found` if record ID is invalid.

- **GET `/attendance/employee/{employeeId}/hours`**
    - **Description**: Calculate total working hours for an employee over a date range.
    - **Request Parameters**:
        - `startDate` (query param): Start date of the range.
        - `endDate` (query param): End date of the range.
    - **Response**:
        - `200 OK` with total working hours.
        - `400 Bad Request` for invalid date range.

- **POST `/attendance/leaves/request`**
    - **Description**: Apply for a leave request.
    - **Request Body**: `LeaveRequest` (startDate, endDate, reason, leaveType).
    - **Request Header**: `employeeId` (required).
    - **Response**:
        - `200 OK` with saved leave request.
        - `400 Bad Request` for invalid input.

- **GET `/attendance/employee/{employeeId}/attendance-count`**
    - **Description**: Get the number of attended days for an employee in a specific month.
    - **Request Parameter**: `month` (query param, format: "yyyy-MM").
    - **Response**:
        - `200 OK` with attendance count.
        - `400 Bad Request` for invalid month format.

- **GET `/attendance/employee/{employeeId}/leave-balance`**
    - **Description**: Check the leave balance for an employee in a specific month.
    - **Request Parameter**: `month` (query param, format: "yyyy-MM").
    - **Response**:
        - `200 OK` with leave balance.
        - `400 Bad Request` for invalid month format.

- **DELETE `/attendance/leaves/{leaveId}`**
    - **Description**: Delete a pending leave request.
    - **Response**:
        - `204 No Content` on success.
        - `400 Bad Request` if leave is not pending or invalid.

- **GET `/attendance/leaves/employee/{employeeId}`**
    - **Description**: Fetch all leave requests for a specific employee.
    - **Request Parameters**:
        - `startDate` (optional, query param): Start date of the range.
        - `endDate` (optional, query param): End date of the range.
    - **Response**:
        - `200 OK` with list of leave requests.
        - `204 No Content` if no leave requests found.
        - `400 Bad Request` if employee not found or invalid date range.

## Prerequisites
- **Java 17** or higher.
- **Maven 3.6+**.
- **PostgreSQL 15+**.
- **Docker** and **Docker Compose** for containerized deployment.
- **NGINX** for API gateway.
- **Eureka Server** for service discovery.

## How to Run the Service

### Local Setup
Follow these steps to run the Attendance Service locally on your machine.

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd Attendance_service

2. **Set Up PostgreSQL**
- Install PostgreSQL if not already installed.
- Create a database named attendance_db using below command:

  `CREATE DATABASE attendance_db;`

- Update the database configuration in src/main/resources/application.properties or application-dev.properties:

`spring.datasource.url=jdbc:postgresql://localhost:5432/{database_name}
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true `

or setup add .env file to Attendance directory root
# .env
# Database credentials for attendance-service
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
# Add postgres for run in docker
SPRING_DATASOURCE_HOST=localhost
SPRING_DATASOURCE_PORT=
SPRING_DATASOURCE_DBNAME=
SERVER_PORT=8081

# Eureka configuration
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka

# User Service URL
USER_SERVICE_URL=http://localhost:8084

3. **Configure Eureka (Optional for Local Setup)**
    - If running locally without Eureka, ensure application.properties has:
      `eureka.client.enabled=false`
      If using Eureka, ensure an Eureka Server is running and update:

   `eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/`

4. **Build the Project**
    - Compile and package the application using Maven:
      `mvn clean install`

5. **Run the Application**
    - Start the application with the dev profile:
      `mvn spring-boot:run -Dspring.profiles.active=dev`

- The service will be available at http://localhost:8081.

- Test the Endpoints
  Use a tool like Postman or cURL to test the API endpoints (e.g., POST /attendance/1).
  Example request:


Note : Make sure to add Bearer token as Authorization header for all requests.
User Access Endpoints

POST /attendance/{employeeId}Record both check-in and check-out for an employee:

curl -X POST "http://localhost:8081/attendance/1" \
-H "Content-Type: application/json" \
-d '{"date": "2025-05-15", "checkInTime": "09:00:00", "checkOutTime": "17:00:00"}'


Notes:
Replace 1 with the actual employeeId.
Expected: 200 OK with the saved attendance record, 400 Bad Request if the input is invalid.


POST /attendance/check-in/{employeeId}Record a check-in for an employee:

curl -X POST "http://localhost:8081/attendance/check-in/1" \
-H "Content-Type: application/json" \
-d '{"date": "2025-05-15", "checkInTime": "09:00:00"}'

Notes:
Replace 1 with the actual employeeId.
Expected: 200 OK with the saved attendance record, 400 Bad Request if the input is invalid.


PUT /attendance/check-out/{recordId}Record a check-out for an existing attendance record:

curl -X PUT "http://localhost:8081/attendance/check-out/1" \
-H "Content-Type: application/json" \
-d '{"checkOutTime": "17:00:00"}'


Notes:
Replace 1 with the actual recordId of an attendance record.
Expected: 200 OK with the updated attendance record, 400 Bad Request if checkOutTime is invalid, 404 Not Found if recordId is invalid.

GET /attendance/{employeeId}Retrieve all attendance records for an employee:

curl -X GET "http://localhost:8081/attendance/1"

Notes:
Replace 1 with the actual employeeId.
Expected: 200 OK with a list of attendance records, 404 Not Found if no records exist.

GET /attendance/hours/{recordId}Calculate working hours for a specific attendance record:

curl -X GET "http://localhost:8081/attendance/hours/1"

Notes:
Replace 1 with the actual recordId.
Expected: 200 OK with the working hours, 400 Bad Request if the record is incomplete, 404 Not Found if recordId is invalid.

GET /attendance/employee/{employeeId}/hoursCalculate total working hours for an employee over a date range:

curl -X GET "http://localhost:8081/attendance/employee/1/hours?startDate=2025-05-01&endDate=2025-05-31"

Notes:
Replace 1 with the actual employeeId.
Adjust the startDate and endDate as needed.

Expected: 200 OK with total working hours, 400 Bad Request if the date range is invalid.
POST /attendance/leaves/requestApply for a leave request:

curl -X POST "http://localhost:8081/attendance/leaves/request" \
-H "employeeId: 1" \
-H "Content-Type: application/json" \
-d '{"startDate": "2025-05-20", "endDate": "2025-05-22", "reason": "Vacation", "leaveType": "ANNUAL"}'


Notes:
Replace 1 with the actual employeeId.
Expected: 200 OK with the saved leave request, 400 Bad Request if the input is invalid.

GET /attendance/employee/{employeeId}/attendance-countGet the number of attended days for an employee in a specific month:

curl -X GET "http://localhost:8081/attendance/employee/1/attendance-count?month=2025-05"

Notes:
Replace 1 with the actual employeeId.
Adjust the month parameter as needed.
Expected: 200 OK with the attendance count, 400 Bad Request if the month format is invalid.


GET /attendance/employee/{employeeId}/leave-balanceCheck the leave balance for an employee in a specific month:

curl -X GET "http://localhost:8081/attendance/employee/1/leave-balance?month=2025-05"

Notes:
Replace 1 with the actual employeeId.
Adjust the month parameter as needed.
Expected: 200 OK with the leave balance, 400 Bad Request if the month format is invalid.

DELETE /attendance/leaves/{leaveId}Delete a pending leave request:

curl -X DELETE "http://localhost:8081/attendance/leaves/1"

Notes:
Replace 1 with the actual leaveId.
Expected: 204 No Content on success, 400 Bad Request if the leave is not pending or invalid.

GET /attendance/leaves/employee/{employeeId}Fetch all leave requests for a specific employee:

curl -X GET "http://localhost:8081/attendance/leaves/employee/1?startDate=2025-05-01&endDate=2025-05-31"

Notes:
Replace 1 with the actual employeeId.
The startDate and endDate parameters are optional; remove them to fetch all leave requests.
Expected: 200 OK with a list of leave requests, 204 No Content if none found, 400 Bad Request if the employee is not found or the date range is invalid.

Admin Access Endpoints

PUT /attendance/leaves/{leaveId}/statusUpdate the status of a leave request (admin only):

curl -X PUT "http://localhost:8081/attendance/leaves/1/status?status=APPROVED" \
-H "employeeId: 2" \
-H "Content-Type: application/json"

Notes:
Replace 1 with the actual leaveId.
Replace 2 with the employeeId of an admin user.
Expected: 200 OK with updated leave details, 403 Forbidden if not an admin, or 404 Not Found if leaveId is invalid.