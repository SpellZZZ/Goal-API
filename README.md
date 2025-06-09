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
