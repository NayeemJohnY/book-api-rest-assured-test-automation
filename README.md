# Book API Testing Framework

A comprehensive framework for automated API testing of a Book system.

[![Test Execution and Publish Report](https://github.com/NayeemJohnY/book-api-rest-assured-test-automation/actions/workflows/test-execution.yml/badge.svg)](https://github.com/NayeemJohnY/book-api-rest-assured-test-automation/actions/workflows/test-execution.yml)

## 🔗 Quick Links

| Resource | Description | Link |
|----------|-------------|------|
| 📋 **Framework Docs** | Complete documentation & project overview | [View Docs](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/index.html) |
| 📊 **Allure Report** | Interactive test execution dashboard | [View Report](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/allure-report/index.html) |
| 📄 **Test Results JSON** | Structured test data for integrations | [View JSON](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/test-results-report.json) |
| 📖 **Java APIDocs** | Main code API documentation | [View Docs](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/javadocs/apidocs/index.html) |
| 🧪 **Test APIDocs** | Test code API documentation | [View Docs](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/javadocs/testapidocs/index.html) |
| 🚀 **CI/CD Pipeline** | GitHub Actions workflow runs | [View Actions](https://github.com/nayeemjohny/book-api-rest-assured-test-automation/actions) |

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
- **Test Groups**: Organized test execution with **smoke**, **regression**, and **negative** test groups for targeted testing strategies
- **Parallel Execution**: Support for parallel test execution to reduce overall test execution time and improve CI/CD pipeline efficiency
- **Retry Logic (retryRequest)**: Automatically retries API requests in configuration tests if a 429 (Too Many Requests) response is received, waiting for the server's `Retry-After` header or a default interval, up to a configurable max retry count.
- **RetryAnalyzer**: TestNG retry analyzer that re-runs failed tests (specifically for HTTP 429) up to a set number of times, with logging and Allure step reporting for each retry attempt.
- **Logger & Custom Logging**: Uses Log4j2 for detailed logging of test execution, including a custom RestAssured filter (`RestAssuredLogFilter`) that logs HTTP requests and responses for every API call, and attaches status codes and retry info to TestNG results for better traceability.
- **Allure Reporting**: Comprehensive test reporting with Allure framework, providing detailed insights into test execution and results
- **CI/CD Ready**: GitHub Actions workflow for automated test execution and reporting
- **Test Results JSON Collection**: Collects all TestNG test execution results, maps them to test case IDs, and exports the aggregated data into a structured JSON file (`test-results/test-results-report.json`). The JSON includes test plan metadata, outcomes, durations, and iteration details for parameterized tests. See **TestResultsRecords.java** and **TestResultsReporter.java**.

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
  - **TestResultLoggerListener.java**: TestNG listener that logs test execution events, results, and integrates with reporting tools.
  - **Assertion.java**: Custom assertion utility that extends TestNG assertions with Log4j2-powered logging, providing clear pass/fail messages in logs and reports.
  - **LoggingMatcher.java**: Integrates Hamcrest matchers with logging for expressive, traceable assertions, making test failures easy to diagnose.
  - **TestResultsReporter.java**: Collects and exports test results to JSON
  - **TestResultsRecords.java**: Data structures for test results JSON
---

## 📁 Project Structure

```
book-api-rest-assured-test-automation/
│
├── .github/                                        # GitHub workflows and actions
│   └── workflows/
│       └── test-execution.yml                      # CI workflow for running tests
├── .gitignore                                      # Git ignore rules
│
├── github-pages/                                   # Static site for documentation/demo
│   ├── index.html
│   └── style.css
│
├── pom.xml                                         # Maven build and dependencies
│
├── README.md                                       # Project documentation
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
│               ├── TestResultLoggerListener.java  # TestNG result logger
│               ├── Assertion.java                 # Custom assertions with logging
│               ├── LoggingMatcher.java            # Hamcrest matcher with logging
│               ├── TestResultsReporter.java       # Collects and exports test results to JSON
│               └── TestResultsRecords.java        # Data structures for test results JSON
│ 
├── test-results/                                  # Test output and logs
│   ├── allure-results/                            # Allure results
│   └── logs/
│       └── books-api-test.log                     # Test execution log
├── testng.xml                                     # TestNG suite configuration
```

## ▶️ Start the Node.js Book API

The Node.js Book API has been moved to a separate repository: [NayeemJohnY/book-nodejs-app](https://github.com/NayeemJohnY/book-nodejs-app).

1. **Clone the Node.js API repository:**
   ```
   git clone https://github.com/NayeemJohnY/book-nodejs-app.git
   cd book-nodejs-app
   ```

2. **Install dependencies:**
   ```
   npm install
   ```

3. **Start the API server:**
   ```
   npm start
   ```
   The API will run at http://localhost:3000/api/books

---

## 🧪 Run API Tests (Maven)

1. Ensure the Node.js API server is running.
2. In the project root, run:

### Run All Tests
```bash
mvn clean test
```

### Run Tests in Parallel
```bash
mvn clean test -Dparallel=true
```

### Run Tests by Groups
```bash
# Run only smoke tests (critical functionality)
mvn clean test -Dgroups=smoke

# Run only regression tests (all tests)
mvn clean test -Dgroups=regression

# Run only negative tests (error handling)
mvn clean test -Dgroups=negative

# Run multiple groups
mvn clean test -Dgroups="smoke,negative"
```

This will execute TestNG tests with the following groups:
- **smoke**: Critical functionality tests (create, read, update, delete with valid data)
- **regression**: All tests to verify existing features
- **negative**: Error handling and validation tests

## 📊 Test Groups

| Group        | Description                          | Example Tests                                     |
| ------------ | ------------------------------------ | ------------------------------------------------- |
| `smoke`      | Critical, basic functionality        | Create book, Get book, Update book, Delete book   |
| `regression` | All tests for comprehensive coverage | All positive and negative scenarios               |
| `negative`   | Error handling and validation        | Invalid data, unauthorized access, missing fields |

## 📊 Test Reports & Documentation

This framework provides comprehensive reporting through multiple channels:

### 📋 Test Results JSON Report
- **Live Report**: [View Test Results JSON](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/test-results-report.json)
- **Format**: Structured JSON with test outcomes, iterations, and detailed execution data
- **Features**: Test case mapping, parameterized test tracking, retry analysis, error categorization
- **Integration**: Perfect for external test management systems and CI/CD dashboards
- **Implementation**: See [`TestResultsReporter.java`](src/test/java/testUtils/TestResultsReporter.java) and [`TestResultsRecords.java`](src/test/java/testUtils/TestResultsRecords.java)

### 📊 Allure Interactive Dashboard  
- **Live Report**: [View Allure Dashboard](https://nayeemjohny.github.io/book-api-rest-assured-test-automation/allure-report/index.html)
- **Features**: Historical trends, test categorization, failure analysis, execution timeline
- **Benefits**: Visual insights, drill-down capabilities, test history tracking
- **How to Generate Locally**:
  ```bash
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


##  GitHub Actions CI/CD

The framework includes a sophisticated GitHub Actions workflow (`test-execution.yml`) that provides automated testing with flexible configuration and comprehensive reporting.

### 🚀 **Workflow Capabilities**
- **Environment Setup**: Automatically configures Java 21+ and Node.js environments
- **API Management**: Deploys and manages the Node.js Book API during test execution
- **Flexible Test Execution**: Support for specific test methods, classes, or groups
- **Parallel Processing**: Configurable parallel execution for faster feedback cycles

### ⚙️ **Configurable Parameters** (Manual Dispatch)
| Parameter | Description | Example Values |
|-----------|-------------|----------------|
| `test_name` | Specific test method/class | `CreateBookTest#testCreateValidBook` |
| `test_group` | Test group filtering | `smoke`, `regression`, `negative` |
| `APP_MAX_REQUESTS` | API rate limiting config | `100` (requests per minute) |
| `parallel` | Enable parallel execution | `true`/`false` |
| `publish_report` | Control report publishing | `true`/`false` |

### 📊 **Multi-Platform Reporting**
The workflow automatically generates and publishes:
- **📊 Allure Reports**: Historical test execution with trend analysis
- **📖 JavaDoc**: API documentation for main and test code  
- **📄 JSON Results**: Structured test data for external integrations
- **� Azure DevOps**: Test results posted to Azure Test Plans
- **🌐 GitHub Pages**: Live deployment of all reports and documentation

### � **Quick Access**
- [🚀 View Workflow Runs](https://github.com/NayeemJohnY/book-api-rest-assured-test-automation/actions)
- [⚡ Manual Dispatch](https://github.com/NayeemJohnY/book-api-rest-assured-test-automation/actions/workflows/test-execution.yml)
- [📋 Workflow Configuration](/.github/workflows/test-execution.yml)

<footer align="center">
  <a href="https://www.linkedin.com/in/nayeemjohny/" target="_blank">Connect with me on LinkedIn</a>
</footer>