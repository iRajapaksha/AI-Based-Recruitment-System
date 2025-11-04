## AI-Based Recruitment System — Backend

A microservices-based backend built with Spring Boot, designed for an AI-powered recruitment system that automates candidate matching, and application tracking.

### Overview

The system helps companies and candidates connect efficiently using AI.
It provides RESTful APIs for job management, candidate profiles, applications, and intelligent AI-based matching.

### Folder Structure
```
AI-Based-Recruitment-System/
│
├── api-gateway/
├── auth-service/
├── service-registry/
├── user-service/
│   ├── src/main/java/com/recruitment/user
│   └── src/main/resources/
├── jobpost-service/
├── organization-service/
├── resume-service/
├── notification-service/
├── screening-service/
└── docker-compose.yml
````

### Key Features

- Microservices architecture using Spring Boot
- AI-based candidate-job matching (using ML/NLP models)
- User authentication & authorization (JWT)
- RESTful APIs for all entities (Users, Jobs, Applications, AI)
- MySQL as the primary database
- Eureka Server for service discovery
- Spring Cloud Gateway for routing & load balancing
- Centralized Configuration using Spring Cloud Config Server
- Docker support for deployment
- CI/CD Ready using GitHub Actions 


### Microservices List
| Microservice         | Description |    
|----------------------|-----------|
| Service Registry     | Service discovery and registration |
| API Gateway          | Routes client requests to respective microservices |
| Auth Service         | Handles registration, login, JWT auth |
| User Service         | Manages user profiles |
| Organization Service | Manages company profiles |
| Jobpost Service      | CRUD operations for job postings |
| Application Service  | Manages candidate applications |
| Notification Service | Handles email notifications |
| Screening Service    | Automated application screening & scheduling |
| Transcript Service   | Store interview transcript|


### Setup & Run
Clone the repository
```
git clone https://github.com/iRajapaksha/AI-Based-Recruitment-System.git
cd AI-Based-Recruitment-System
````
Start the Discovery Services
```
cd ../service-registry
mvn spring-boot:run
```

Start API Gateway
```
cd ../api-gateway
mvn spring-boot:run
```
Start each microservice

```
cd ../user-service
mvn spring-boot:run

cd ../jobpost-service
mvn spring-boot:run

cd ../resume-service
mvn spring-boot:run

cd ../organization-service
mvn spring-boot:run

cd ../notification-service
mvn spring-boot:run

cd ../screening-service
mvn spring-boot:run

cd ../auth-service
mvn spring-boot:run
```


### Docker Setup 

```
cd AI-Based-Recruitment-System
docker-compose up -d
```

Services will be accessible at:

Gateway: http://localhost:8080

Eureka Dashboard: http://localhost:8761


###  Author

- Ishara Rajapaksha
- Computer Engineering Undergraduate, University of Ruhuna
