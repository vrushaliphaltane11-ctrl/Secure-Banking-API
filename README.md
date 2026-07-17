# 🏦 Secure Banking API

A secure banking backend application built using **Java 17**, **Spring Boot**, **Spring Security**, **JWT Authentication**, **Hibernate/JPA**, and **MySQL**.

This project demonstrates secure authentication, role-based authorization, banking transactions, exception handling, and RESTful API development following enterprise backend development practices.

---

## 🚀 Features

- 🔐 User Registration
- 🔑 User Login with JWT Authentication
- 🛡️ Spring Security Integration
- 👥 Role-Based Authorization (Customer, Employee, Admin)
- 🔒 BCrypt Password Encryption
- 🏦 Bank Account Management
- 💰 Deposit Money
- 💸 Withdraw Money
- 🔄 Transaction Processing
- ⚠️ Global Exception Handling
- ✅ Request Validation
- 📄 Swagger/OpenAPI Documentation
- 🗄️ MySQL Database Integration
- 🔁 Transaction Management using `@Transactional`

---

## 🛠️ Tech Stack

| Technology | Version |
|------------|---------|
| Java | 17 |
| Spring Boot | 4.1 |
| Spring Security | Latest |
| JWT | JJWT |
| Hibernate / JPA | Latest |
| MySQL | 8.0 |
| Maven | Latest |
| Swagger / OpenAPI | SpringDoc |
| Lombok | Latest |

---

## 📂 Project Structure

```
src
 ├── config
 ├── controller
 ├── dto
 ├── entity
 ├── exception
 ├── repository
 ├── security
 ├── service
 └── util
```

---

## 🔐 Authentication

The application uses **JWT (JSON Web Token)** for authentication.

### Register

```
POST /api/auth/register
```

### Login

```
POST /api/auth/login
```

After successful login, include the JWT token in the Authorization header:

```
Authorization: Bearer <eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ1VTVE9NRVIiLCJ1c2VySWQiOjEsInN1YiI6InZydXNoYWxpQGV4YW1wbGUuY29tIiwiaWF0IjoxNzg0MzA5NzMxLCJleHAiOjE3ODQzMTMzMzF9.fklIMZg2qoqpBSJKDzPv6i2OFf4udmP5n1X-chKwSPs>
```

---

## 💳 Banking APIs

### Deposit

```
POST /api/transactions/deposit
```

Example Request

```json
{
  "accountNumber": "100000",
  "amount": 2000,
  "description": "Salary Credit"
}
```

---

### Withdraw

```
POST /api/transactions/withdraw
```

Example Request

```json
{
  "accountNumber": "100000",
  "amount": 500,
  "description": "ATM Withdrawal"
}
```

---

## ⚠️ Exception Handling

Custom exceptions implemented:

- EmailAlreadyExistsException
- InvalidCredentialsException
- AccountNotFoundException
- InvalidTransactionException
- InsufficientBalanceException

---

## 📷 Screenshots

### Swagger UI

> <img width="1306" height="598" alt="Swagger 1" src="https://github.com/user-attachments/assets/82107598-de4a-44da-bcda-0a4e039459a4" />


### Login API



<img width="1343" height="644" alt="Swagger Login1" src="https://github.com/user-attachments/assets/3ef8e64b-0201-411c-820b-b81f566b1e8c" />

<img width="1308" height="323" alt="Swagger login 2" src="https://github.com/user-attachments/assets/2ebdce0d-394a-4f2b-8835-c6dd69261f04" />

### Deposit Transaction

> <img width="801" height="347" alt="Transaction Deposit Swagger Testing" src="https://github.com/user-attachments/assets/6ce408d3-b71b-4322-a019-3d345971c333" />


### Withdraw Transaction

> <img width="630" height="461" alt="Money Withdrawl" src="https://github.com/user-attachments/assets/7ee8267e-6e97-4752-b35b-a694a63420fa" />

### Negative Testing for Insufficient Account Balance
<img width="627" height="409" alt="Negative Test_Large Withdrawl_Insufficient Balance" src="https://github.com/user-attachments/assets/38bd5a24-f537-4e5b-933d-5452ad0e739b" />

### MySQL Database

> <img width="854" height="182" alt="SQL Bank account" src="https://github.com/user-attachments/assets/78211eb2-8415-4fa7-97f4-42eab7095261" />
<img width="1166" height="716" alt="MySQL USer registered" src="https://github.com/user-attachments/assets/2aa90bb9-d777-48f5-80ec-77a8c9b339b2" />
PAssword Encrypted while storing
<img width="1340" height="358" alt="SQL Password encryption" src="https://github.com/user-attachments/assets/ed9939dc-acc8-4fd7-893a-e8b6bd802310" />

---

## ▶️ Running the Project

Clone the repository

```bash
git clone https://github.com/vrushaliphaltane11-ctrl/Secure-Banking-API.git
```

Navigate to project

```bash
cd Secure-Banking-API
```

Configure MySQL in:

```
application.properties
```

Run the application

```bash
mvn spring-boot:run
```

Open Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## 👩‍💻 Author

**Vrushali Phaltane**
  portfolio link:https://vrushali-phaltane.vercel.app/
- GitHub: https://github.com/vrushaliphaltane11-ctrl
- LinkedIn: https://www.linkedin.com/in/vrushali-phaltane-a45263164/

---

## ⭐ Future Enhancements

- Fund Transfer API
- Transaction History
- Account Statement
- Docker Support
- Unit Testing (JUnit & Mockito)
- Microservices Architecture
- Redis Caching
- Kafka Integration

---

## 📄 License

This project is created for learning, portfolio, and interview preparation purposes.
