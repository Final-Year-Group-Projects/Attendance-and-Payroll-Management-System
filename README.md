

6. **Docker Deployment**

   - Follow these steps to deploy the Attendance Service using Docker, along with NGINX and Eureka.

       - Build the Docker Image
         - Ensure you have a Dockerfile in the project root. Example Dockerfile:
           dockerfile
           `FROM openjdk:17-jdk-slim
           WORKDIR /app
           COPY target/Attendance_service-0.0.1-SNAPSHOT.jar app.jar
           ENTRYPOINT ["java", "-jar", "app.jar"]`

         - Build the Docker image:

            `docker build -t attendance-service:latest .`
         - 
7. **Set Up Docker Compose**
   - Create a docker-compose.yml file to orchestrate the services (PostgreSQL, Eureka, NGINX, and Attendance Service):
    ```version: '3.8'
    services:
    postgres:
    image: postgres:15
    environment:
    POSTGRES_DB: attendance_db
    POSTGRES_USER: your_username
    POSTGRES_PASSWORD: your_password
    ports:
         - "5432:5432"
         volumes:
         - postgres_data:/var/lib/postgresql/data
    
    eureka:
    image: springcloud/eureka
    ports:
    - "8761:8761"
    environment:
      - EUREKA_SERVER_ADDRESS=http://eureka:8761/eureka/
    
    attendance-service:
    image: attendance-service:latest
    depends_on:
    - postgres
      - eureka
      environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/attendance_db
      - SPRING_DATASOURCE_USERNAME=your_username
      - SPRING_DATASOURCE_PASSWORD=your_password
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka:8761/eureka/
      ports:
      - "8080:8080"
    
    nginx:
    image: nginx:latest
    ports:
    - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      depends_on:
      - attendance-service
    
    volumes:
    postgres_data:```
      

- Create an nginx.conf file for the API gateway:
      `events {}
      http {
        upstream attendance_service {
        server attendance-service:8080;
      }
      server {
        listen 80;
        location / {
            proxy_pass http://attendance_service;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            }
        }
      }`

8. **Run Docker Compose**
   - Start all services:

    `docker-compose up -d`

    - Verify the services are running:
   
     `docker-compose ps`

    - Access the Service
   
    The Attendance Service will be available through NGINX at http://localhost/attendance/....
    Test the endpoints using a tool like Postman or cURL.
    Stop the Services

    - To stop and remove the containers:

     `docker-compose down`