# ✅ BankHub Testing Checklist

## **Pre-Deployment Verification**

### **Infrastructure Verification**

- [ ] PostgreSQL is running
  ```bash
  docker-compose exec postgres pg_isready
  ```
  
- [ ] Redis is running
  ```bash
  docker-compose exec redis redis-cli ping
  ```
  
- [ ] Kafka is running
  ```bash
  docker-compose exec kafka kafka-broker-api-versions --bootstrap-server localhost:9092
  ```
  
- [ ] All Docker containers are healthy
  ```bash
  docker-compose ps
  ```

---

### **Service Build Verification**

- [ ] Discovery Server builds without errors
  ```bash
  cd discovery-server && mvn clean package -DskipTests
  ```
  
- [ ] API Gateway builds without errors
  ```bash
  cd api-gateway && mvn clean package -DskipTests
  ```
  
- [ ] User Service builds without errors
  ```bash
  cd user-service && mvn clean package -DskipTests
  ```
  
- [ ] Account Service builds without errors
  ```bash
  cd account-service && mvn clean package -DskipTests
  ```
  
- [ ] Transaction Service builds without errors
  ```bash
  cd transaction-service && mvn clean package -DskipTests
  ```
  
- [ ] Fraud Service builds without errors
  ```bash
  cd fraud-service && mvn clean package -DskipTests
  ```
  
- [ ] Notification Service builds without errors
  ```bash
  cd notification-service && mvn clean package -DskipTests
  ```

---

### **Service Startup Verification**

- [ ] Discovery Server starts successfully
  - Check: http://localhost:8761
  - Verify: Eureka Dashboard loads
  
- [ ] API Gateway starts successfully
  - Check: http://localhost:8080/health
  - Verify: Returns 200 OK
  
- [ ] User Service starts successfully
  - Check: http://localhost:8081/actuator/health
  - Verify: Returns 200 OK
  
- [ ] Account Service starts successfully
  - Check: http://localhost:8082/actuator/health
  - Verify: Returns 200 OK
  
- [ ] Transaction Service starts successfully
  - Check: http://localhost:8083/actuator/health
  - Verify: Returns 200 OK
  
- [ ] Fraud Service starts successfully
  - Check: http://localhost:8084/actuator/health
  - Verify: Returns 200 OK
  
- [ ] Notification Service starts successfully
  - Check: http://localhost:8085/actuator/health
  - Verify: Returns 200 OK

---

## **User Service Testing**

### **User Registration**

- [ ] Register user with valid data
  ```bash
  curl -X POST http://localhost:8080/api/users/register \
    -H "Content-Type: application/json" \
    -d '{
      "username": "testuser",
      "email": "test@example.com",
      "password": "TestPass123!",
      "firstName": "Test",
      "lastName": "User"
    }'
  ```
  Expected: 201 Created

- [ ] Attempt to register duplicate username
  Expected: 409 Conflict or 400 Bad Request

- [ ] Attempt to register with invalid email
  Expected: 400 Bad Request

- [ ] Attempt to register with weak password
  Expected: 400 Bad Request

### **User Login**

- [ ] Login with valid credentials
  ```bash
  curl -X POST http://localhost:8080/api/users/login \
    -H "Content-Type: application/json" \
    -d '{"username": "testuser", "password": "TestPass123!"}'
  ```
  Expected: 200 OK with accessToken and refreshToken

- [ ] Login with invalid username
  Expected: 401 Unauthorized

- [ ] Login with invalid password
  Expected: 401 Unauthorized

### **User Profile**

- [ ] Get user profile with valid token
  ```bash
  curl -X GET http://localhost:8080/api/users/profile \
    -H "Authorization: Bearer {accessToken}"
  ```
  Expected: 200 OK with user data

- [ ] Get user profile without token
  Expected: 401 Unauthorized

- [ ] Get user profile with invalid token
  Expected: 401 Unauthorized

---

## **Account Service Testing**

### **Account Creation**

- [ ] Create checking account
  ```bash
  curl -X POST http://localhost:8080/api/accounts \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer {accessToken}" \
    -d '{
      "userId": 1,
      "accountType": "CHECKING",
      "initialDeposit": 1000.00
    }'
  ```
  Expected: 201 Created with account details

- [ ] Create savings account
  Expected: 201 Created

- [ ] Create credit account
  Expected: 201 Created

- [ ] Create account with negative balance
  Expected: 400 Bad Request

### **Account Retrieval**

- [ ] Get account by account number
  Expected: 200 OK with account details

- [ ] Get all accounts for user
  Expected: 200 OK with account list

- [ ] Get non-existent account
  Expected: 404 Not Found

### **Account Balance Operations**

- [ ] Check account balance
  Expected: 200 OK with balance

- [ ] Verify balance after deposit
  Expected: Balance increased

- [ ] Verify balance after withdrawal
  Expected: Balance decreased

---

## **Transaction Service Testing**

### **Money Transfer**

- [ ] Transfer money between accounts
  ```bash
  curl -X POST http://localhost:8080/api/transactions/transfer \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer {accessToken}" \
    -d '{
      "fromAccount": "ACC001",
      "toAccount": "ACC002",
      "amount": 500.00,
      "description": "Test transfer",
      "userId": 1
    }'
  ```
  Expected: 201 Created with transaction details

- [ ] Transfer with insufficient funds
  Expected: 400 Bad Request

- [ ] Transfer negative amount
  Expected: 400 Bad Request

- [ ] Transfer to non-existent account
  Expected: 400 Bad Request

### **Transaction History**

- [ ] Get transaction history for user
  Expected: 200 OK with transaction list

- [ ] Get transactions for specific account
  Expected: 200 OK with transaction list

- [ ] Verify transaction details
  Expected: All transaction fields present

- [ ] Check transaction status
  Expected: Status is COMPLETED/PENDING/FAILED

### **Kafka Event Publishing**

- [ ] Verify transaction event published to Kafka
  ```bash
  docker-compose exec kafka kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic transaction-events \
    --from-beginning
  ```
  Expected: Transaction event appears in topic

---

## **Fraud Service Testing**

### **Fraud Analysis**

- [ ] Analyze low-risk transaction
  ```bash
  curl -X POST http://localhost:8080/api/fraud/analyze \
    -H "Content-Type: application/json" \
    -d '{
      "transactionId": "TXN001",
      "userId": 1,
      "fromAccount": "ACC001",
      "toAccount": "ACC002",
      "amount": 100.00,
      "transactionType": "TRANSFER",
      "timestamp": "2024-03-11T10:30:00"
    }'
  ```
  Expected: 200 OK with low risk score

- [ ] Analyze high-risk transaction
  - High amount transfer
  - Expected: Risk score > 75, Decision: BLOCKED

- [ ] Analyze unusual pattern
  - Transfer at unusual hour
  - Expected: Risk factors identified

### **Fraud Statistics**

- [ ] Get fraud statistics
  ```bash
  curl -X GET http://localhost:8080/api/fraud/stats
  ```
  Expected: 200 OK with fraud metrics

- [ ] Verify fraud detection rate
  Expected: Statistics show detection metrics

### **Kafka Event Publishing**

- [ ] Verify fraud event published for blocked transactions
  ```bash
  docker-compose exec kafka kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic fraud-events \
    --from-beginning
  ```
  Expected: Fraud alert event appears in topic

---

## **Notification Service Testing**

### **Email Notifications**

- [ ] Verify transaction email notification received
  - Trigger transaction
  - Expected: Email content logged/displayed

- [ ] Verify fraud alert email
  - Trigger fraud detection
  - Expected: Fraud alert email content shown

- [ ] Check email format
  - Expected: Proper formatting and readability

### **SMS Notifications**

- [ ] Verify SMS sent for high-risk fraud
  - Expected: SMS format displayed

- [ ] Check SMS content
  - Expected: Account last 4 digits, amount, risk level

### **In-App Notifications**

- [ ] Verify in-app notification stored
  - Expected: Notification data persisted

- [ ] Check notification retrieval
  - Expected: Notifications retrievable via API

### **Kafka Event Consumption**

- [ ] Verify Notification Service consumes transaction events
  - Expected: Logs show event processing

- [ ] Verify Notification Service consumes fraud events
  - Expected: Logs show fraud alert processing

---

## **Integration Testing**

### **End-to-End Workflow**

- [ ] **Step 1:** Create user account
  - Expected: User created successfully

- [ ] **Step 2:** Login and get token
  - Expected: JWT token received

- [ ] **Step 3:** Create bank account
  - Expected: Account created with initial balance

- [ ] **Step 4:** Create second account
  - Expected: Second account created

- [ ] **Step 5:** Transfer money
  - Expected: Transaction created and event published

- [ ] **Step 6:** Verify notification sent
  - Expected: Transaction notification logged

- [ ] **Step 7:** Check fraud detection
  - Expected: Transaction analyzed for fraud

- [ ] **Step 8:** Verify transaction history
  - Expected: Transaction appears in history

### **Cross-Service Communication**

- [ ] User Service → Account Service
  - Expected: Accounts associated with user

- [ ] Account Service → Transaction Service
  - Expected: Transactions tracked per account

- [ ] Transaction Service → Kafka
  - Expected: Events published successfully

- [ ] Kafka → Notification Service
  - Expected: Events consumed and processed

---

## **Performance Testing**

- [ ] API Gateway response time < 200ms
  - Expected: Fast request handling

- [ ] Fraud analysis latency < 500ms
  - Expected: Real-time fraud detection

- [ ] Concurrent user registrations (10 users)
  - Expected: All succeed without errors

- [ ] Concurrent transactions (5 transfers)
  - Expected: No race conditions

- [ ] Kafka message processing delay < 1s
  - Expected: Near real-time notifications

---

## **Security Testing**

- [ ] JWT token validation
  - Expired token rejected
  - Invalid token rejected

- [ ] Password encryption
  - Passwords stored hashed
  - Plain text not visible in database

- [ ] CORS validation
  - Requests from allowed origins accepted
  - Requests from blocked origins rejected

- [ ] SQL injection prevention
  - Malicious SQL input rejected
  - Parameterized queries used

- [ ] Request validation
  - Invalid input rejected
  - XSS attacks prevented

---

## **Database Testing**

### **PostgreSQL**

- [ ] User table created and populated
  ```bash
  docker-compose exec postgres psql -U postgres -d bankhub -c "SELECT COUNT(*) FROM users;"
  ```
  Expected: Rows present

- [ ] Account table created and populated
  Expected: Account records present

- [ ] Transaction table created and populated
  Expected: Transaction records present

- [ ] Fraud check table created
  Expected: Fraud check records present

### **Redis**

- [ ] Session cache working
  ```bash
  docker-compose exec redis redis-cli KEYS "*session*"
  ```
  Expected: Session keys present

- [ ] Cache hit rate > 80%
  Expected: Efficient caching

---

## **Error Handling Testing**

- [ ] 400 Bad Request for invalid input
  Expected: Proper error message

- [ ] 401 Unauthorized without token
  Expected: Proper error message

- [ ] 404 Not Found for missing resource
  Expected: Proper error message

- [ ] 500 Internal Server Error handling
  Expected: Graceful error response

- [ ] Connection timeout handling
  Expected: Service recovers gracefully

---

## **Monitoring & Logging**

- [ ] All services logging correctly
  ```bash
  tail -f {service-name}/logs/application.log
  ```
  Expected: Log entries present

- [ ] Eureka dashboard shows all services
  - Expected: 7 services registered

- [ ] Health check endpoints respond
  Expected: All return 200 OK

- [ ] Metrics available in actuator
  Expected: Metrics data accessible

---

## **Cleanup Procedures**

- [ ] Database can be reset
  ```bash
  docker-compose down -v
  ```
  Expected: All data cleared

- [ ] Services restart cleanly
  Expected: No residual state

- [ ] No zombie processes
  Expected: All processes terminated

---

## **Final Sign-Off**

| Item | Status | Notes |
|------|--------|-------|
| All services build successfully | ☐ | |
| All services start without errors | ☐ | |
| User registration works | ☐ | |
| User login works | ☐ | |
| Account creation works | ☐ | |
| Money transfer works | ☐ | |
| Fraud detection works | ☐ | |
| Notifications sent | ☐ | |
| Kafka events published | ☐ | |
| Database operations work | ☐ | |
| Performance acceptable | ☐ | |
| Security requirements met | ☐ | |
| Documentation complete | ☐ | |
| **READY FOR DEPLOYMENT** | ☐ | |

---

**Tested By:** _________________  
**Date:** _________________  
**Environment:** Local Development  
**Java Version:** 21+  
**Status:** ☐ PASSED / ☐ FAILED

---

Last Updated: March 11, 2026

