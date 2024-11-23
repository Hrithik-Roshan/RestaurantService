Restaurant Service
The Restaurant Service is a microservice built with Java Spring Boot and powered by a MySQL database, designed to manage restaurant information and menu items efficiently. It provides a suite of APIs to handle CRUD operations on restaurants and menus, including restaurant details like name, location, operating hours, and categorized menu items (e.g., appetizers, main courses, desserts). It also integrates with the Order Service to update order statuses dynamically.

Features
Restaurant Management:

Add, update, view, and manage restaurant details.
Track restaurant status (e.g., open, closed, accepting orders).
Menu Management:

Add, update, delete, and retrieve categorized menu items.
Properties include name, price, category, and availability.
Integration:

Supports dynamic updates to orders via integration with the Order Service.
Built-in Security:

Easily extensible to support OAuth/token-based security.
Prerequisites
Before running the project locally, ensure the following software is installed:

Java JDK 17
Apache Maven (for dependency management)
MySQL Server (for database)
Postman (optional, for testing APIs)
Git (to clone the repository)
Getting Started
Follow these steps to set up and run the project locally.

1. Clone the Repository
bash
Copy code
git clone https://github.com/<your-username>/restaurant-service.git
cd restaurant-service
2. Configure the Database
Create a MySQL database:
sql
Copy code
CREATE DATABASE restaurant_service;
Update the database configuration in src/main/resources/application.properties:
properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_service
spring.datasource.username=<your_mysql_username>
spring.datasource.password=<your_mysql_password>
spring.jpa.hibernate.ddl-auto=update
3. Build the Application
Use Maven to build the application:

bash
Copy code
mvn clean install
4. Run the Application
Start the Spring Boot application:

bash
Copy code
mvn spring-boot:run
The application will start on http://localhost:8088.

5. Access the APIs
API Documentation: Open the Swagger UI at http://localhost:8088/swagger-ui/index.html.