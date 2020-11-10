# Youtube Monitor
### An application to monitor most popular youtube videos by Region
##### To run the application you should have java 8 install on you machine
##### Run command mvnw spring-boot:run in your terminal application will become available at localhost:8080

### Used Stack
* Java 8
* Maven
* React
* Spring Boot
* H2 in memory DB
* Spring JPA + Hibernate
* Spring web for REST APIs
* JWT for stateless authorization
* Websockets for a server to client communication
* Youtube Data API for data gathering
* Mockito and Spring boot test framework for automated tests

### Architectural Decisions worth mentioning
* Every youtube request for internal data update is delegated to the new thread and transaction. This is guarantee for low latency and logical state in the db.
* Updates in the backend are pushed to the active clients using websockets. This means no busy waiting from the client side.
 
