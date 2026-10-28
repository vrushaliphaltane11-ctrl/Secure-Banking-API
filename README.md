# 🏦 Secure Banking API

A secure banking backend application built using **Java 17**, **Spring Boot**,**Redis** **Spring Security**, **JWT Authentication**, **Hibernate/JPA**, and **MySQL**.

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

## Redis Caching

Redis was implemented to improve the performance of frequently accessed account data and reduce unnecessary database queries.

### Caching Strategy

The application follows the **Cache-Aside (Lazy Loading) Pattern**:

```text
Client
  |
  | GET /api/accounts/{accountNumber}
  v
AccountService
  |
  |-- Check Redis
  |      |
  |      |-- Cache HIT ------> Return cached AccountResponse
  |      |
  |      |-- Cache MISS
  |             |
  |             v
  |           MySQL
  |             |
  |             v
  |       AccountResponse
  |             |
  |             v
  |        Store in Redis
  |        TTL = 5 minutes
  |             |
  |             v
  |          Response
```

### Redis Implementation

For account retrieval, Redis is checked before querying MySQL.

The Redis key follows this format:

```text
account:{accountNumber}
```

For example:

```text
account:100000
```

When the account is not present in Redis, the application retrieves it from MySQL and stores the resulting `AccountResponse` in Redis with a **5-minute TTL**.

```java
String key = "account:" + accountNumber;

String cachedAccount = redisTemplate.opsForValue().get(key);

if (cachedAccount != null) {
    log.info("CACHE HIT for account: {}", accountNumber);

    return objectMapper.readValue(
            cachedAccount,
            AccountResponse.class
    );
}

log.info(
        "CACHE MISS for account: {}. Fetching from MySQL.",
        accountNumber
);

BankAccount account = accountRepository
        .findByAccountNumber(accountNumber)
        .orElseThrow(() ->
                new AccountNotFoundException(
                        "Account not found: " + accountNumber
                )
        );

AccountResponse response = AccountResponse.builder()
        .id(account.getId())
        .accountNumber(account.getAccountNumber())
        .accountType(account.getAccountType())
        .balance(account.getBalance())
        .status(account.getStatus())
        .build();

String json = objectMapper.writeValueAsString(response);

redisTemplate.opsForValue().set(
        key,
        json,
        Duration.ofMinutes(5)
);
```

### Cache Miss and Cache Hit Verification

The Redis implementation was tested using account number `100000`.

First, the existing Redis key was removed:

```powershell
docker exec -it redis-banking redis-cli DEL account:100000
```

Redis returned:

```text
(integer) 1
```

This confirms that the existing `account:100000` cache entry was successfully deleted.

The account API was then called:

```text
GET /api/accounts/100000
```

The application log showed:

```text
CACHE MISS for account: 100000. Fetching from MySQL.
Account 100000 cached in Redis for 5 minutes
```

This confirms that the application:

1. Checked Redis.
2. Detected a cache miss.
3. Retrieved the account from MySQL.
4. Stored the account response in Redis.
5. Applied a 5-minute TTL.

The Redis cache was then verified directly:

```powershell
docker exec -it redis-banking redis-cli GET account:100000
```

Example result:

```text
"{\"accountNumber\":\"100000\",\"accountType\":\"CURRENT\",\"balance\":13600.00,\"id\":1,\"status\":\"ACTIVE\"}"
```

The TTL was also verified:

```powershell
docker exec -it redis-banking redis-cli TTL account:100000
```

The returned value represents the remaining lifetime of the cache entry in seconds.

On a subsequent request for the same account, the application logs showed:

```text
CACHE HIT for account: 100000
```

This confirms that the second request was served from Redis instead of querying MySQL.

### Redis Benefits

* Reduces repeated database queries.
* Improves response time for frequently accessed accounts.
* Uses account number as a predictable cache key.
* Prevents stale cache entries from remaining indefinitely through a 5-minute TTL.
* Provides observable cache HIT/MISS logging.
* Redis runs as a Docker container for local development.

### Technology Used

* **Spring Boot**
* **Spring Data Redis**
* **StringRedisTemplate**
* **Redis**
* **Docker**
* **MySQL**
* **JPA/Hibernate**
* **Jackson**
* **SLF4J/Lombok Logging**


## 📷 Screenshots

### Swagger UI

> <img width="1306" height="598" alt="Swagger 1" src="https://github.com/user-attachments/assets/82107598-de4a-44da-bcda-0a4e039459a4" />


### Deposit Transaction

> <img width="801" height="347" alt="Transaction Deposit Swagger Testing" src="https://github.com/user-attachments/assets/6ce408d3-b71b-4322-a019-3d345971c333" />


### Withdraw Transaction

> <img width="630" height="461" alt="Money Withdrawl" src="https://github.com/user-attachments/assets/7ee8267e-6e97-4752-b35b-a694a63420fa" />

### Negative Testing for Insufficient Account Balance
<img width="627" height="409" alt="Negative Test_Large Withdrawl_Insufficient Balance" src="https://github.com/user-attachments/assets/38bd5a24-f537-4e5b-933d-5452ad0e739b" />

### MySQL Database

> <img width="854" height="182" alt="SQL Bank account" src="https://github.com/user-attachments/assets/78211eb2-8415-4fa7-97f4-42eab7095261" />
<img width="1166" height="716" alt="MySQL USer registered" src="https://github.com/user-attachments/assets/2aa90bb9-d777-48f5-80ec-77a8c9b339b2" />


Password Encrypted while storing


<img width="1340" height="358" alt="SQL Password encryption" src="https://github.com/user-attachments/assets/ed9939dc-acc8-4fd7-893a-e8b6bd802310" />

---

## REDIS CACHING

Cache Miss  and Cached data from redis

<img width="1348" height="95" alt="cach combine" src="https://github.com/user-attachments/assets/11746bb4-6474-4021-9ded-adf55333ae86" />

Screenshot 2





<img width="1159" height="210" alt="Redis get account" src="https://github.com/user-attachments/assets/43134ebc-c5f3-40f4-b9af-ad37ee59c4b4" />

Screenshot 3

<img width="1263" height="240" alt="ttl" src="https://github.com/user-attachments/assets/7a5986ba-03d4-4506-8bf2-29f350dfd808" />

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
