# Attendance and Payroll Management System
This project is a distributed system for managing attendance and payroll, built using Spring Boot microservices, PostgreSQL databases, Eureka Server for service discovery, and an NGINX API gateway. It uses Docker Compose to orchestrate and manage all services for local and cloud-agnostic deployment.

## Project Architecture
The system follows a microservices architecture with clear service separation:

1. [x] **API Gateway (NGINX)** : Acts as the reverse proxy, routing client requests to appropriate services.
2. [x] **Eureka Server** : Service discovery server allowing services to register themselves and discover others dynamically.
3. [x] **Spring Boot Microservices:**

   * attendance-service: Manages employee attendance data.

   * auth-service: Manages authentication and authorization.

   * user-service: Manages user profile and HR-related data.

   * payroll-service: Handles payroll calculations and processing.

4. [x] **PostgreSQL Databases:**
Each service has its own dedicated PostgreSQL database for data isolation and scalability:

    * attendance-db
    * auth-db
    * user-db
    * payroll-db

Docker Compose manages and links all containers within a dedicated Docker network for clean, reproducible deployments.
![img.png](img.png)
---
### Prerequisites

Before you begin, ensure you have the following installed on your machine:
* Docker: Install Docker Desktop (includes Docker Compose) for your operating system:
Windows/Mac: Download from Docker Desktop
Linux: Follow the Docker installation guide
* Git: To clone the repository. Install from Git Downloads.
A terminal (e.g., Command Prompt, PowerShell, or Bash).
* PostgreSQL Client (optional): Install psql or a GUI like pgAdmin if you plan to access the databases internally (e.g., sudo apt install postgresql-client on Ubuntu).

### Setup Instructions

Follow these steps to set up and run the project:

1. Clone the Repository

    ```bash
    git clone https://github.com/Final-Year-Group-Projects/Attendance-and-Payroll-Management-System
    ```

2. Create the .env File
    
    A .env.example file has been added to the codebase. Copy it to create your environment file:


3. Build and Run the Services

    Use Docker Compose to build and start all services (Eureka server, application services, databases, and API gateway).

    Run the following command in the project directory:
    ```bash
    docker-compose up -d --build
    ```

This will start:
Eureka server (eureka) on port 8761.
Application services (attendance-service, auth-service, user-service, payroll-service) internally on ports 8081, 8080, 8084, and 8082.
PostgreSQL databases (attendance-db, auth-db, user-db, payroll-db) internally (not exposed to the host).
API gateway (api-gateway) on port 80.

4. Verify the Services Are Running

    Check the status of the containers to ensure they’re running:
    ```bash
    docker-compose ps
    ```

    All services should show a state of Up (e.g., Up (healthy) for services with health checks).
    If any service is not running, check the logs for errors:
    ```bash
    docker-compose logs <service-name>
    ```
5. Access the Services
Eureka Dashboard: Open http://localhost:8761 in your browser to see the service registry. You should see attendance_service, auth_service, user_service, and payroll_service listed.
API Gateway: Access the application through the NGINX gateway at http://localhost. The gateway routes requests to the appropriate services based on the nginx.conf configuration.
Databases: The database services are not exposed to the host (ports like 5432 are removed for security). To access them:


   
Enter the container shell for the desired database:

docker exec -it <db-container-name> sh

Example: docker exec -it attendance-db sh



Inside the container, run psql with the appropriate user and database:

psql -U $POSTGRES_USER -d $POSTGRES_DB

Replace $POSTGRES_USER and $POSTGRES_DB with the values from your .env file (e.g., psql -U postgres -d attendance_db).


Alternatively, install a PostgreSQL client on your host and connect via the Docker network by running a container with psql:

docker run -it --rm --network app-network postgres:15 psql -h attendance-db -U $SPRING_DATASOURCE_USERNAME -d $ATTENDANCE_DB_NAME

Adjust the host (attendance-db, auth-db, etc.) and database name based on the service.

6. Stop the Services

To stop all services, run:

docker-compose down


This stops and removes the containers but preserves the database data (stored in Docker volumes: attendance-data, auth-data, user-data, payroll-data).
To also remove the volumes (and lose all database data), add the -v flag:

docker-compose down -v