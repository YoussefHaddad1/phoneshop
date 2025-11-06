Phoneshop API
Description

Phoneshop is a Spring Boot based RESTful API designed to manage a basic inventory system for phones, brands, and customers. The API allows users to:

Manage phone records (Create, Read, Update, Delete)

Manage brand records (Create, Read, Update, Delete)

Manage customer records (Create, Read, Update, Delete)

This project implements a basic CRUD application for a phone shop with a database using MySQL.

Features

Brands: CRUD operations for brands.

Phones: CRUD operations for phones, each linked to a brand.

Customers: CRUD operations for customers.

Foreign Key Constraints: Phones are associated with brands, and brand IDs must be valid when creating or updating phones.

Technologies Used

Backend Framework: Spring Boot (v3.5.7)

Database: MySQL (8.0.43)

JPA: Hibernate ORM

Dependency Management: Maven

Build Tool: Maven

Testing: Postman for API testing

Setup & Installation

Follow these steps to run the project locally:

1. Clone the repository
git clone https://github.com/yourusername/phoneshop.git
cd phoneshop

2. Set up MySQL Database

Ensure that MySQL is installed and running locally. You can create a new database phoneshop using the following command:

CREATE DATABASE phoneshop;

3. Configure application.properties

Update the src/main/resources/application.properties file with your MySQL credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/phoneshop
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

4. Build the project

Make sure you have Maven installed. Then, run the following command to build the project:

mvn clean install

5. Run the application

Start the Spring Boot application:

mvn spring-boot:run


The application will run on localhost:8080.

6. Test with Postman

Import the Postman collection provided in the project to test the API endpoints. Ensure the base URL in Postman is set to:

http://localhost:8080

7. API Endpoints

Below are the API endpoints exposed by the application:

Brands

POST /api/brands: Create a new brand

GET /api/brands: Get all brands

GET /api/brands/{id}: Get a brand by ID

PUT /api/brands/{id}: Update an existing brand

DELETE /api/brands/{id}: Delete a brand

Phones

POST /api/phones: Add a new phone

GET /api/phones: Get all phones

GET /api/phones/{id}: Get a phone by ID

PUT /api/phones/{id}: Update an existing phone

DELETE /api/phones/{id}: Delete a phone

Customers

POST /api/customers: Create a new customer

GET /api/customers: Get all customers

GET /api/customers/{id}: Get a customer by ID

PUT /api/customers/{id}: Update an existing customer

DELETE /api/customers/{id}: Delete a customer

8. Database Schema

The application uses the following database schema:

Brands: Holds information about phone brands.

Phones: Stores information about phones, with a foreign key to the brands table.

Customers: Stores customer details.

9. Testing with Postman

Postman tests for the API can be run by importing the provided Postman Collection. The following tests are included:

Brand Tests: Creating, updating, listing, and deleting brands.

Phone Tests: Creating, updating, listing, and deleting phones.

Customer Tests: Creating, updating, listing, and deleting customers.

10. Troubleshooting

500 Errors (SQLIntegrityConstraintViolationException): Ensure that brand IDs exist when creating or updating phones, as phones are linked to brands through foreign key constraints.

405 Method Not Allowed: Make sure you're using the correct HTTP method (POST, GET, PUT, DELETE).

License

This project is licensed under the MIT License - see the LICENSE
 file for details.

Contributing

Feel free to fork the repository, open issues, and create pull requests. Contributions are welcome!
