# Goal Api

This project is a microservices base system built with Java, Spring Boot, and Docker. 
It consists of three core services: 

* API Gateway - acts as the system’s entry point, routing incoming client requests to the appropriate internal services.
* Discovery Service - service discovery module, likely based on Netflix Eureka, allowing other services to register and discover each other.
* Goal Service -  implements business logic (e.g., goals, payments, priorities.


The Goal Service handles business logic related to goals, payments, periods, and priorities. It is organized into layers including controllers, services, repositories, and domain models. The project uses Maven for dependency management and build automation. Liquibase is used for database versioning and schema management. Each service is containerized, and Docker Compose is used to run the system as a whole. The architecture promotes scalability, modularity, and ease of deployment.

#### Package structure:

* controller: REST controllers (e.g., GoalController.java, PaymentController.java)

* dto: Data transfer objects

* model: JPA entities (Goal, Payment, etc.)

* service: Business logic layer (interfaces and implementations)

* repo: JPA repositories

* Additional utility layers: mapper, util, exception, filter, specification

#### Additional features:

* Custom code style configuration (checkstyle.xml)

* Unit tests (controller tests, SQL test data scripts)

* Liquibase configuration (db.changelog/scripts)

#### Security
Keycloak serves as an external identity provider, handling user authentication and role-based access control. The goal-service microservice is integrated with Keycloak via Spring Security, enabling JWT token validation and secure access to protected resources. This setup provides centralized and secure authentication across the microservices architecture.


### Run  
```
git clone https://github.com/SpellZZZ/file-server.git
```
Then 
```
docker compose up -d
```
File docker-compose.yml is used to run all microservices and supporting components like Keycloak in a unified environment. It defines containers, networks, and environment variables for each service, simplifying local testing and deployment. With Docker Compose, the entire system can be launched simultaneously with a single command.
