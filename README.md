# Banking System API

A RESTful API for managing users and bank accounts using **Spring Boot** with **JWT** for security and **PostgreSQL** as the database.

---


## Features

- User registration and authentication with JWT  
- One-to-many relationship between users and bank accounts  
- Role-based access control  
- Data validation and error handling  

Secure transactions using JWT ownership check + user-account relation
- Linked BankAccount to User via @ManyToOne relation
- Added ownership check in deposit/withdraw logic using JWT
- Created endpoint to fetch current user's accounts (/api/accounts/my)
- Prevented unauthorized access to others' accounts

---

## Technologies Used

- Java 21  
- Spring Boot  
- Spring Security with JWT  
- Hibernate / JPA  
- PostgreSQL  
- Maven  
- Lombok  

---

## Getting Started

### Prerequisites

- Java 21
- Maven  
- PostgreSQL
