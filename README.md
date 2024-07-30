# customer-management-service
Utility service for customer-management

# `Customer Management Spring Boot Application`

# `Overview`

This project is a Spring Boot application designed to manage customer information. It provides RESTful APIs to create and retrieve customers, as well as to compare two lists of customers to find unique and common entries. It also incorporates Kafka for messaging and uses an H2 in-memory database for data storage.

# `Features`

Create Customer: API to create new customer records.
Retrieve Customers: API to retrieve customers by name, city, or state, or all customers if no parameters are provided.
Compare Customer Lists: API to compare two lists of customers to find customers only in list A, only in list B, and in both lists.
Kafka Integration: Sends a message to a Kafka topic after a customer is created.
Validation: Uses `@Valid` annotation for input validation.
Example Matcher: Utilizes ExampleMatcher for querying customers.
Transactional Operations: Ensures atomicity of customer creation using `@Transactional` annotation.
Application Versioning: Appends /v1 to API endpoints for versioning.

# Technologies

    `Spring Boot
    Spring Data JPA
    Spring Web
    Spring Validation
    Spring Kafka
    H2 Database
    SLF4J for logging
    JUnit 5 for testing`

# How to Run

# Clone the Repository:

`git clone https://github.com/baghelenishchal/customer-management-service.git`

`cd ./customer-management-service`

# Build the Project:
`mvn clean install`

# Run the Application:
`mvn spring-boot:run`
