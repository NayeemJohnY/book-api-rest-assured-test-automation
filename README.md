# Book API Testing Framework

A comprehensive framework for automated API testing of a Book system.

[![Test Execution and Publish Report](https://github.com/NayeemJohnY/book-api-rest-assured-test-automation/actions/workflows/test-execution.yml/badge.svg)](https://github.com/NayeemJohnY/book-api-rest-assured-test-automation/actions/workflows/test-execution.yml)

## Quick Links

:link: [Framework Docs](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/index.html)

:link: [Sample Allure Report](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/allure-report/index.html)

:link: [Java APIDocs](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/javadocs/apidocs/index.html)

:link: [Test APIDocs](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/javadocs/testapidocs/index.html)

---
## Tech Stack

| Label          | Tool                                    |
| -------------- | --------------------------------------- |
| Language       | Java 21+                                |
| API Testing    | Rest Assured                            |
| Test Framework | TestNG                                  |
| Reporting      | Allure                                  |
| Logging        | Log4j2                                  |
| JSON           | Jackson (serialization/deserialization) |
| Assertions     | Hamcrest                                |
| Build Tool     | Maven                                   |


## 🚀 Key Functionalities

- **Node.js Book API**: RESTful API for books (CRUD, search, pagination, auth, rate limiting)
- **Java Test Suite**: TestNG + RestAssured for automated API tests (Create, Get, Update, Delete)
- **Retry Logic (retryRequest)**: Automatically retries API requests in configuration tests if a 429 (Too Many Requests) response is received, waiting for the server's `Retry-After` header or a default interval, up to a configurable max retry count.
- **RetryAnalyzer**: TestNG retry analyzer that re-runs failed tests (specifically for HTTP 429) up to a set number of times, with logging and Allure step reporting for each retry attempt.
- **Logger & Custom Logging**: Uses Log4j2 for detailed logging of test execution, including a custom RestAssured filter (`RestAssuredLogFilter`) that logs HTTP requests and responses for every API call, and attaches status codes and retry info to TestNG results for better traceability.
- **Allure Reporting**: Beautiful, interactive test reports with step-by-step details and retry history.
- **CI/CD Ready:** Includes a sample GitHub Actions workflow for automated test execution and reporting.

---

## 📒 Key Classes & Utilities

- **Book.java**: POJO representing a book entity (id, title, author) used for API payloads and responses.
- **JsonUtils.java**: Utility class for JSON serialization/deserialization, simplifying conversion between Java objects and JSON strings.
- **RestAssuredLogFilter.java**: Custom RestAssured filter that logs HTTP requests and responses using Log4j2, and attaches status codes and retry info to TestNG results for traceability.
- **BaseTest.java**: Abstract base class for all API tests. Provides:
  - Test suite and method setup/teardown
  - Centralized RestAssured configuration
  - `retryRequest` method for handling rate-limiting (HTTP 429)
  - Logging and context management
  - Automatic cleanup/reset of test data after suite
- **Test Utils (testUtils/):**
  - **AnnotationTransformer.java**: Dynamically modifies TestNG annotations at runtime (e.g., to apply retry logic or listeners).
  - **RetryAnalyzer.java**: Implements TestNG's retry logic for flaky tests, especially for HTTP 429, with logging and Allure integration.
  - **TestResultsLogListener.java**: TestNG listener that logs test execution events, results, and integrates with reporting tools.
  - **Assertion.java**: Custom assertion utility that extends TestNG assertions with Log4j2-powered logging, providing clear pass/fail messages in logs and reports.
  - **LoggingMatcher.java**: Integrates Hamcrest matchers with logging for expressive, traceable assertions, making test failures easy to diagnose.
---

## 📁 Project Structure


```
book-api-rest-assured-test-automation/
│
│
├── .github/                                        # GitHub workflows and actions
│   └── workflows/
│       └── test-execution.yml                      # CI workflow for running tests
├── .gitignore                                      # Git ignore rules
│
│
├── books-nodejs-app/                               # Node.js mock Book API server
│   ├── index.js                                    # Express API implementation
│   ├── package.json                                # Node.js dependencies & scripts
│   └── package-lock.json                           # NPM lock file
│
├── github-pages/                                   # Static site for documentation/demo
│   ├── index.html
│   └── style.css
│
├── pom.xml                                        # Maven build and dependencies
│
├── README.md                                      # Project documentation
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── pojos/
│   │   │   │   └── Book.java                      # Book POJO for API payloads
│   │   │   └── utils/
│   │   │       ├── JsonUtils.java                 # JSON utility methods
│   │   │       └── RestAssuredLogFilter.java      # Custom RestAssured logging
│   │   │
│   │   └── resources/
│   │       └── log4j2.xml                         # Logging configuration
│   │   
│   └── test/
│       └── java/
│           ├── tests/
│           │   ├── BaseTest.java                  # Test base class
│           │   ├── TS01_CreateBook.java           # Test: Create Book
│           │   ├── TS02_GetBook.java              # Test: Get Book
│           │   ├── TS03_UpdateBook.java           # Test: Update Book
│           │   └── TS04_DeleteBook.java           # Test: Delete Book
│           │
│           └── testUtils/
│               ├── AnnotationTransformer.java     # TestNG annotation helper
│               ├── RetryAnalyzer.java             # Retry logic for flaky tests
│               └── TestResultsLogListener.java    # TestNG result logger
│ 
├── test-results/                                  # Test output and logs
│   ├── allure-results/                            # Allure results
│   └── logs/
│       └── libarary-books-api-test.log            # Test execution log
├── testng.xml                                     # TestNG suite configuration
```

## ▶️ Start the Node.js Book API

1. Open a terminal and navigate to `books-nodejs-app`:
   ```
   cd books-nodejs-app
   ```
2. Install dependencies:
   ```
   npm install
   ```
3. Start the API server:
   ```
   npm start
   ```
   The API will run at http://localhost:3000/api/books

---

## 🧪 Run API Tests (Maven)

1. Ensure the Node.js API server is running.
2. In the project root, run:
   ```
   mvn clean test
   ```
   This will execute all TestNG tests defined in `testng.xml`.

---

## 📊 Generate Allure Report

1. After running tests, generate the Allure report:
   ```
   allure generate test-results/allure-results --clean -o test-results/allure-report
   allure open test-results/allure-report
   ```
   (Requires Allure CLI installed and in your PATH.)

---

## 🔑 Authentication Tokens

- User Token: `user-token` (for normal operations)
- Admin Token: `admin-token` (for admin/delete operations)
- Pass as Bearer token in `Authorization` header (e.g., `Bearer user-token`)

---

## 📚 Example API Endpoints

- `GET /api/books` — List books
- `POST /api/books` — Create book (auth required)
- `PUT /api/books/:id` — Update book (auth required)
- `DELETE /api/books/:id` — Delete book (admin auth required)

---


<footer align="center">
  <a href="https://www.linkedin.com/in/nayeemjohny/" target="_blank">Connect with me on LinkedIn</a>
</footer>

