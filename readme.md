# Online Book Store

---

## Introduction

The Online Book Store project was inspired by the need for an efficient online platform for book enthusiasts to explore, shop for, and manage their book collections. The aim is to provide a user-friendly interface for shoppers and robust tools for managers to organize and monitor the store.

## Technologies and Tools

- **Spring Boot:** A Java-based framework for building web applications.
- **Spring Security:** Provides authentication and authorization capabilities.
- **Spring Data JPA:** Simplifies database interactions within Hibernate.
- **Swagger:** Allows for API documentation and testing.
- **MySQL:** Used as the relational database to store book and user data.
- **Liquibase:** Library for tracking, managing, and applying database schema changes.
- **Docker:** A platform for delivering software in containers.

## Functionalities

### Shoppers (User)

#### Authentication:

- Users can register and log in to their accounts.

#### Browsing:

- View all available books.
- Search for books by name or category.
- Explore bookshelf sections.

#### Shopping Cart:

- Add and remove books from the shopping cart.
- View the contents of the shopping cart.

#### Order Placement:

- Purchase the items in the shopping cart.
- View past order receipts.

#### Receipts:

- See all books on a receipt.
- View details of individual books on a receipt.

### Managers (Admin)

#### Book Management:

- Add new books to the store.
- Edit book details.
- Remove books from the store.

#### Category Management:

- Create and manage bookshelf sections.

#### Receipt Management:

- Change the status of order receipts (e.g., "Shipped" or "Delivered").

## Setup and Usage

### Environment Configuration (.env)

To set up your environment, create a `.env` file in the project root directory and add the following content:

```.env
# MySQL Configuration
MYSQL_DB=online_book_store
MYSQL_LOCAL_PORT=3307 
MYSQL_DOCKER_PORT=3306
MYSQL_PING_CHECK=mysqladmin ping -u root -p$MYSQL_ROOT_PASSWORD
COMMAND_PROMPT=CMD-SHELL

# Spring Boot Configuration
SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005

# Data Generation Configuration
GENERATE_DATA=sAdmin, demo

# Database Credentials
MYSQL_ROOT_PASSWORD=YourRootPassword
MYSQL_USER=YourDatabaseUser
MYSQL_PASSWORD=YourDatabasePassword
```

- **MySQL Configuration:** This section configures MySQL, including port settings, ping check with the root password, and command prompt configuration.

- **Spring Boot Configuration:** This section configures Spring Boot, specifying local and Docker ports, and a debug port.

- **Data Generation Configuration (GENERATE_DATA):** The `GENERATE_DATA` property allows you to load specific data during setup. In the provided template, you can load data for 'sAdmin' (an admin user) and 'demo' (books, categories, and users).

Make sure to replace `YourRootPassword`, `YourDatabaseUser`, and `YourDatabasePassword` with appropriate values.

### User Credentials

To access the preloaded data, use the following credentials:

- **sAdmin (Admin User):**
   - Email: sAdmin@book.store
   - Password: Test1234

- **Demo Users:**
   - Email: harry@example.com
   - Password: Test1234

   - Email: frodo@example.com
   - Password: Test1234

   - Email: luke@example.com
   - Password: Test1234

### Running the Project

1. Clone the project from GitHub: [Online Book Store GitHub Repository](https://github.com/Dimagaa/online-book-store.git).

2. Ensure you have Java 17 and Docker installed on your system.

3. Open a terminal and navigate to the project directory.

4. Run the following commands:

   ```
   mvn clean install
   docker-compose up
   ```

   This command will start the project, including the MySQL database, Spring Boot application, and other required services.

5. To access and test the API using Swagger, please ensure to use the appropriate port. By default, the Spring Boot application may run on port 8088, but the port can be configured in the `.env` file under `SPRING_LOCAL_PORT` and `SPRING_DOCKER_PORT`. If you've customized the port configuration in the `.env` file, replace `8088` in the Swagger URL accordingly.

    - Swagger Documentation URL: [Swagger Documentation](http://localhost:<PORT>/api/swagger-ui/index.html#/)

6. Alternatively, you can use Postman for API testing: [Online Book Store Postman Collection](https://www.postman.com/flight-geoscientist-49978449/workspace/online-book-store/overview).

## Challenges and Solutions

### Challenge 1: Implementing Soft Deleted Records

**Challenge:** Handling the deletion of records while retaining them for historical purposes.

**Solution:** Implemented soft deletion by adding an "is_deleted" flag to database tables. Modified application logic to exclude soft-deleted records from regular queries.

### Challenge 2: Managing Database Schema Changes with Liquibase

**Challenge:** Making changes to the database schema consistently and trackably.

**Solution:** Employed Liquibase to define and version database schema changes. Applied changes with backups and rollback strategies in place.

By addressing these challenges, we enhanced the reliability, security, and maintainability of our Online Book Store project.

---
