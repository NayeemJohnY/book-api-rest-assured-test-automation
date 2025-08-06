# Library API Testing Framework

A comprehensive framework for automated API testing of a Book Library system.

---

## 🚀 Key Functionalities

- **Node.js Book API**: RESTful API for books (CRUD, search, pagination, auth, rate limiting)
- **Java Test Suite**: TestNG + RestAssured for automated API tests (Create, Get, Update, Delete)
- **Retry Logic (retryRequest)**: Automatically retries API requests in tests if a 429 (Too Many Requests) response is received, waiting for the server's `Retry-After` header or a default interval, up to a configurable max retry count.
- **RetryAnalyzer**: TestNG retry analyzer that re-runs failed tests (specifically for HTTP 429) up to a set number of times, with logging and Allure step reporting for each retry attempt.
- **Logger & Custom Logging**: Uses Log4j2 for detailed logging of test execution, including a custom RestAssured filter (`RestAssuredLogFilter`) that logs HTTP requests and responses for every API call, and attaches status codes and retry info to TestNG results for better traceability.
- **Allure Reporting**: Beautiful, interactive test reports with step-by-step details and retry history.

---

## 📁 Project Structure


```
LibraryAPITestingFramework/
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
