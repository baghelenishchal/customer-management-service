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

# Create Customer (problem 1 part a)

URL: /api/v1/customers
Method: POST
Request Body: 
[{
    "customer": {
        "firstName": "John",
        "lastName": "Doe",
        "customerId": "12345",
        "age": 30,
        "spendingLimit": 450000.001244,
        "mobileNumber": "1234567890",
        "address": [{
            "type": "Home",
            "address1": "123 Street",
            "address2": "Apt 1",
            "city": "City",
            "state": "State",
            "zipCode": "12345"
        }]
    }
}]
Response: 201 Created

# Retrieve Customers (problem 1 part b)

URL: /api/v1/customers
Method: GET
Query Parameters: firstName, city, state
Response:
json
Copy code
[
    {
        "firstName": "John",
        "lastName": "Doe",
        "customerId": "12345",
        "age": 30,
        "spendingLimit": 450000.001244,
        "mobileNumber": "1234567890",
        "address": [{
            "type": "Home",
            "address1": "123 Street",
            "address2": "Apt 1",
            "city": "City",
            "state": "State",
            "zipCode": "12345"
        }]
    }
]

# Compare Customer Lists (problem 2)

URL: /api/v1/customers/compare
Method: POST
Request Body:
{
    "listA": [
        {
            "firstName": "John",
            "lastName": "Doe",
            "customerId": "12345",
            "age": 30,
            "spendingLimit": 450000.001244,
            "mobileNumber": "1234567890",
            "address": []
        },
        {
            "firstName": "Jane",
            "lastName": "Doe",
            "customerId": "67890",
            "age": 25,
            "spendingLimit": 250000.001244,
            "mobileNumber": "0987654321",
            "address": []
        }
    ],
    "listB": [
        {
            "firstName": "Jane",
            "lastName": "Doe",
            "customerId": "67890",
            "age": 25,
            "spendingLimit": 250000.001244,
            "mobileNumber": "0987654321",
            "address": []
        },
        {
            "firstName": "Alice",
            "lastName": "Smith",
            "customerId": "11111",
            "age": 28,
            "spendingLimit": 150000.001244,
            "mobileNumber": "5555555555",
            "address": []
        }
    ]
}

Response:
{
    "onlyInA": [
        {
            "firstName": "John",
            "lastName": "Doe",
            "customerId": "12345",
            "age": 30,
            "spendingLimit": 450000.001244,
            "mobileNumber": "1234567890",
            "address": []
        }
    ],
    "onlyInB": [
        {
            "firstName": "Alice",
            "lastName": "Smith",
            "customerId": "11111",
            "age": 28,
            "spendingLimit": 150000.001244,
            "mobileNumber": "5555555555",
            "address": []
        }
    ],
    "inBoth": [
        {
            "firstName": "Jane",
            "lastName": "Doe",
            "customerId": "67890",
            "age": 25,
            "spendingLimit": 250000.001244,
            "mobileNumber": "0987654321",
            "address": []
        }
    ]
}
