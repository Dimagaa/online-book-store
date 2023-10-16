# Online Book Store

---

## Introduction

The Online Book Store project was inspired by the need for an efficient online platform for book enthusiasts to explore,
shop for, and manage their book collections. The aim is to provide a user-friendly interface for shoppers and robust
tools for managers to organize and monitor the store.

## Technologies and Tools

- **Spring Boot:** A Java-based framework for building web applications.
- **Spring Security:** Provides authentication and authorization capabilities.
- **Spring Data JPA:** Simplifies database interactions within Hibernate.
- **Swagger:** Allows for API documentation and testing.
- **MySQL:** Used as the relational database to store book and user data.
- **Liquibase:** Library for tracking, managing, and applying database schema changes.
- **Docker:** A platform for delivering software in containers.

## Video Presentation

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/je94b48P8GA/0.jpg)](https://youtu.be/je94b48P8GA?si=BjrNyUUWF0zb-knZ)

## Functionalities

### Shoppers (User)

#### Authentication:

The API uses token-based authentication. You must obtain a JSON Web Token (JWT) and include it in the Authorization
header for protected routes.

- **Login**:
    - **Endpoint**: `/auth/login`
  
    - **Request**:
        - **Method**: POST
        - **Request Body**:

      ```json
      {
        "email": "user@example.com",
        "password": "YourPassword123"
      }
      ```

  - **Response**:
      - **Status Code**: 200 OK
      - **Response Body**:

      ```json
      {
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzQWRtaW5AYm9vay5zdG9yZSIsImlhdCI6MTY5NzM2NzUxMSwiZXhwIjoxNjk3MzN6k7y0DAqdLpVj2-F2R4Nxst52CMHsADscIKQ9gqKrXHRBhew"
      }
      ```

- **Register**:
    - **Endpoint**: `/auth/register`
    - **Request**:
        - **Method**: POST
        - **Request Body**:

      ```json
      {
        "email": "bob@email.com",
        "password": "Test1234",
        "repeatPassword": "Test1234",
        "firstName": "Bob",
        "lastName": "Miller",
        "shippingAddress": "JS6740023 Chicago, USA"
      }
      ```

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

      ```json
        {
          "id": 5,
          "email": "bob@email.com",
          "firstName": "Bob",
          "lastName": "Miller",
          "shippingAddress": "JS6740023 Chicago, USA"
        }
        ```

#### Browsing Books and Categories

- **Browse All Books**:
    - **Endpoint**: `/books`
    - **Request**:
        - **Method**: GET
        - **Request Params**:
            - `page` (optional): The page number for pagination.
            - `size` (optional): The number of items per page.
            - `sort` (optional): Sorting criteria.

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          [
              {
                  "id": 3,
                  "title": "Pride and Prejudice",
                  "author": "Jane Austen",
                  "isbn": "9780486284736",
                  "price": 7.99,
                  "description": "A romantic novel that follows the life and relationships of Elizabeth Bennet in 19th-century England.",
                  "coverImage": "https://www.example.com/images/book3.jpg",
                  "categories": [19, 27]
              },
              {
                  "id": 4,
                  "title": "The Great Gatsby",
                  "author": "F. Scott Fitzgerald",
                  "isbn": "9780743273565",
                  "price": 8.75,
                  "description": "A novel set in the Roaring Twenties, depicting the decadence and disillusionment of the American Dream.",
                  "coverImage": "https://www.example.com/images/book4.jpg",
                  "categories": [20, 28]
              }
          ]
          ```

- **View Book Details**:
    - **Endpoint**: `/books/{id}`
    - **Request**:
        - **Method**: GET

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 1,
              "title": "To Kill a Mockingbird",
              "author": "Harper Lee",
              "isbn": "9780061120084",
              "price": 9.99,
              "description": "A classic novel that explores themes of racial injustice and moral growth in the American South.",
              "coverImage": "https://www.example.com/images/book1.jpg",
              "categories": [16, 26, 12]
          }
          ```

- **Search for Books**:
    - **Endpoint**: `/books/search?title={title}&author={author}&isbn={isbn}`
    - **Request**:
        - **Method**: GET
        - **Request Params**:
            - `title` (optional): The title of the book to search for.
            - `author` (optional): The author of the book to search for.
            - `isbn` (optional): The ISBN of the book to search for.
            - `sort` (optional): Sorting criteria.
            - `page` (optional): The page number for pagination.
            - `size` (optional): The number of items per page.

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**: (Example JSON response body is the same as the "Browse All Books" endpoint.)

- **Browse Bookshelf Sections**:
    - **Endpoint**: `/categories`
    - **Request**:
        - **Method**: GET

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          [
              {
                  "id": 1,
                  "name": "Fantasy Adventure",
                  "description": "Fantasy adventure books"
              },
              {
                  "id": 2,
                  "name": "Dystopian Fiction",
                  "description": "Novels in a dystopian setting"
              },
              {
                  "id": 3,
                  "name": "Post-Apocalyptic Fiction",
                  "description": "Post-apocalyptic novels"
              }
          ]
          ```

- **Get Category by ID**:
    - **Endpoint**: `/categories/{id}`
    - **Request**:
        - **Method**: GET

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 1,
              "name": "Fantasy Adventure",
              "description": "Fantasy adventure books"
          }
          ```

- **Get Books by Category ID**:
    - **Endpoint**: `/categories/{id}/books`
    - **Request**:
        - **Method**: GET

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**: (Example JSON response body is the same as the "Browse All Books" endpoint.)

#### Shopping Cart

- **Add New Cart Item**:
    - **Endpoint**: `/cart`
    - **Request**:
        - **Method**: POST
        - **Request Body**:

          ```json
          {
              "bookId": "3",
              "quantity": "2"
          }
          ```

    - **Response**:
        - **Status Code**: 201 Created
        - **Response Body**:

          ```json
          {
              "id": 1,
              "bookId": 3,
              "bookTitle": "Pride and Prejudice",
              "quantity": 2
          }
          ```

- **Update Cart Item**:
    - **Endpoint**: `/cart/cart-items/{id}`
    - **Request**:
        - **Method**: PUT
        - **Request Body**:

          ```json
          {
              "quantity": "1"
          }
          ```

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 1,
              "bookId": 3,
              "bookTitle": "Pride and Prejudice",
              "quantity": 1
          }
          ```

- **Delete Cart Item**:
    - **Endpoint**: `/cart-items/{id}`
    - **Request**:
        - **Method**: DELETE

    - **Response**:
        - **Status Code**: 204 No Content

- **Get Shopping Cart**:
    - **Endpoint**: `/cart`
    - **Request**:
        - **Method**: GET

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 1,
              "userId": 3,
              "cartItems": [
                  {
                      "id": 3,
                      "bookId": 5,
                      "bookTitle": "To the Lighthouse",
                      "quantity": 2
                  },
                  {
                      "id": 2,
                      "bookId": 3,
                      "bookTitle": "Pride and Prejudice",
                      "quantity": 2
                  }
              ]
          }
          ```

#### Order

- **Place an Order**:
    - **Endpoint**: `/orders`
    - **Request**:
        - **Method**: POST
        - **Request Body**:

          ```json
          {
              "shippingAddress": "JS6740023 Chicago, USA"
          }
          ```

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 1,
              "userId": 3,
              "orderItems": [
                  {
                      "id": 1,
                      "bookId": 5,
                      "quantity": 2
                  }
              ],
              "orderDate": "2023-10-15T12:02:51.316180965",
              "total": 40.48,
              "status": "PENDING"
          }
          ```

- **Get Order History**:
    - **Endpoint**: `/orders`
    - **Request**:
        - **Method**: GET

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          [
              {
                  "id": 1,
                  "userId": 3,
                  "orderItems": [
                      {
                          "id": 1,
                          "bookId": 5,
                          "quantity": 2
                      }
                  ],
                  "orderDate": "2023-10-15T12:02:51",
                  "total": 40.48,
                  "status": "CONFIRMED"
              },
              {
                  "id": 2,
                  "userId": 3,
                  "orderItems": [
                      {
                          "id": 3,
                          "bookId": 24,
                          "quantity": 1
                      }
                  ],
                  "orderDate": "2023-10-15T12:06:20",
                  "total": 12.25,
                  "status": "PENDING"
              }
          ]
          ```

- **Get Specific Order Item**:
    - **Endpoint**: `/orders/{order_id}/items/{order_item_id}`
    - **Request**:
        - **Method**: GET

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 1,
              "bookId": 5,
              "quantity": 2
          }
          ```

- **Get Order Items for Specific Order**:
    - **Endpoint**: `/orders/{order_id}/items`
    - **Request**:
        - **Method**: GET

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          [
              {
                  "id": 1,
                  "bookId": 5,
                  "quantity": 2
              },
              {
                  "id": 2,
                  "bookId": 3,
                  "quantity": 2
              }
          ]
          ```

### Managers (Admin)

#### Book Management

- **Create a New Book**:
    - **Endpoint**: `/books`
    - **Request**:
        - **Method**: POST
        - **Request Body**:

          ```json
          {
            "title": "The Shinning",
            "author": "Stephen King",
            "isbn": "9780385121675",
            "price": 29.99,
            "description": "Jack and his family move into an isolated hotel with a violent past. Living in isolation, Jack begins to lose his sanity, which affects his family members",
            "coverImage": "https://example.com/cover3.jpg",
            "categories": [
                "2", "1"
            ]
          }
          ```

    - **Response**:
        - **Status Code**: 201 Created
        - **Response Body**:

          ```json
          {
              "id": 32,
              "title": "The Shinning",
              "author": "Stephen King",
              "isbn": "9780385121675",
              "price": 29.99,
              "description": "Jack and his family move into an isolated hotel with a violent past. Living in isolation, Jack begins to lose his sanity, which affects his family members",
              "coverImage": "https://example.com/cover3.jpg",
              "categories": [
                  1,
                  2
              ]
          }
          ```

- **Update a Book**:
    - **Endpoint**: `/books/{book_id}`
    - **Request**:
        - **Method**: PUT
        - **Request Body**:

          ```json
          {
            "title": "The Shinning",
            "author": "Stephen King",
            "isbn": "9780385121675",
            "price": 29.99,
            "description": "Jack and his family move into an isolated hotel with a violent past. Living in isolation, Jack begins to lose his sanity, which affects his family members",
            "coverImage": "https://example.com/cover3.jpg",
            "categories": [
                "2"
            ]
          }
          ```

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 32,
              "title": "The Shinning",
              "author": "Stephen King",
              "isbn": "9780385121675",
              "price": 29.99,
              "description": "Jack and his family move into an isolated hotel with a violent past. Living in isolation, Jack begins to lose his sanity, which affects his family members",
              "coverImage": "https://example.com/cover3.jpg",
              "categories": [
                  2
              ]
          }
          ```

- **Delete a Book**:
    - **Endpoint**: `/books/{book_id}`
    - **Request**:
        - **Method**: DELETE

    - **Response**:
        - **Status Code**: 204 No Content

#### Category Management

- **Create a New Category**:
    - **Endpoint**: `/categories`
    - **Request**:
        - **Method**: POST
        - **Request Body**:

          ```json
          {
              "name": "Horror",
              "description": "Horror book"
          }
          ```

    - **Response**:
        - **Status Code**: 201 Created
        - **Response Body**:

          ```json
          {
              "id": 29,
              "name": "Horror",
              "description": "Horror book"
          }
          ```

- **Update a Category**:
    - **Endpoint**: `/categories/{category_id}`
    - **Request**:
        - **Method**: PUT
        - **Request Body**:

          ```json
          {
              "name": "Horror fiction",
              "description": "Horror is a genre of fiction that is intended to disturb, frighten or scare"
          }
          ```

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 29,
              "name": "Horror fiction",
              "description": "Horror is a genre of fiction that is intended to disturb, frighten or scare"
          }
          ```

- **Delete a Category**:
    - **Endpoint**: `/categories/{category_id}`
    - **Request**:
        - **Method**: DELETE

    - **Response**:
        - **Status Code**: 204 No Content

#### Order Management

- **Update Order Status**:
    - **Endpoint**: `/orders/{order_id}`
    - **Request**:
        - **Method**: PATCH
        - **Request Body**:

          ```json
          {
              "status": "CONFIRMED"
          }
          ```

    - **Response**:
        - **Status Code**: 200 OK
        - **Response Body**:

          ```json
          {
              "id": 1,
              "userId": 3,
              "orderItems": [
                  {
                      "id": 2,
                      "bookId": 3,
                      "quantity": 2
                  }
              ],
              "orderDate": "2023-10-15T12:02:51",
              "total": 40.48,
              "status": "CONFIRMED"
          }
          ```

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

- **MySQL Configuration:** This section configures MySQL, including port settings, ping check with the root password,
  and command prompt configuration.

- **Spring Boot Configuration:** This section configures Spring Boot, specifying local and Docker ports, and a debug
  port.

- **Data Generation Configuration (GENERATE_DATA):** The `GENERATE_DATA` property allows you to load specific data
  during setup. In the provided template, you can load data for 'sAdmin' (an admin user) and 'demo' (books, categories,
  and users).

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

1. Clone the project from
   GitHub: [Online Book Store GitHub Repository](https://github.com/Dimagaa/online-book-store.git).

2. Ensure you have Java 17 and Docker installed on your system.

3. Open a terminal and navigate to the project directory.

4. Run the following commands:

   ```
   mvn clean install
   docker-compose build
   docker-compose up
   ```

   This command will start the project, including the MySQL database, Spring Boot application, and other required
   services.

5. To access and test the API using Swagger, please ensure to use the appropriate port. By default, the Spring Boot
   application may run on port 8088, but the port can be configured in the `.env` file under `SPRING_LOCAL_PORT`
   and `SPRING_DOCKER_PORT`. If you've customized the port configuration in the `.env` file, replace `8088` in the
   Swagger URL accordingly.

    - Swagger Documentation URL: [Swagger Documentation](http://localhost:<PORT>/api/swagger-ui/index.html#/)

6. Alternatively, you can use Postman for API
   testing: [Online Book Store Postman Collection](https://www.postman.com/flight-geoscientist-49978449/workspace/online-book-store/overview).

## Challenges and Solutions

### Challenge 1: Implementing Soft Deleted Records

**Challenge:** Handling the deletion of records while retaining them for historical purposes.

**Solution:** Implemented soft deletion by adding an "is_deleted" flag to database tables. Modified application logic to
exclude soft-deleted records from regular queries.

### Challenge 2: Managing Database Schema Changes with Liquibase

**Challenge:** Making changes to the database schema consistently and trackably.

**Solution:** Employed Liquibase to define and version database schema changes. Applied changes with backups and
rollback strategies in place.

By addressing these challenges, we enhanced the reliability, security, and maintainability of our Online Book Store
project.

### Contributions

We welcome contributions from the community to help enhance and improve the Online Book Store project. Whether you're
interested in fixing bugs, adding new features, or improving documentation, your contributions are valuable.
---
