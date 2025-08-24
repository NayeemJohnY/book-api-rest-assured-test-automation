/**
 * About test-results collection into a JSON:
 * 
 * This reporter collects all TestNG test execution results, maps them to test case IDs,
 * and exports the aggregated data into a structured JSON file (`test-results/test-results-report.json`).
 * The JSON includes test plan metadata, outcomes, durations, and iteration details for parameterized tests.
 */
package testUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import testUtils.TestResultsRecords.*;
import utils.JsonUtils;

/**
 * Custom TestNG reporter that generates structured JSON reports for test execution results.
 *
 * <p>This reporter implements the {@link IReporter} interface to capture comprehensive test
 * execution data and export it as JSON for integration with external test management systems and
 * CI/CD pipelines.
 *
 * <h3>Key Features:</h3>
 *
 * <ul>
 *   <li>Maps test methods to test case IDs using a configuration file
 *   <li>Handles both single execution and parameterized tests with multiple iterations
 *   <li>Captures test outcomes, execution durations, and detailed parameters
 *   <li>Exports results in structured JSON format
 *   <li>Provides null safety and error handling
 * </ul>
 *
 * <h3>Configuration:</h3>
 *
 * <p>The reporter expects a {@code test-plan-suite.json} file in the test resources directory with
 * the following structure:
 *
 * <pre>{@code
 * {
 *   "testPlanName": "Automation Test Plan",
 *   "testSuiteName": "API Test Suite",
 *   "testCases": {
 *     "testMethodName": {
 *       "testCaseId": "TC-001"
 *     }
 *   }
 * }
 * }</pre>
 *
 * <h3>Output:</h3>
 *
 * <p>Generates a JSON report at {@code test-results/test-results-report.json} containing test
 * execution summary and detailed results for each test case.
 *
 * @author Nayeem John Y
 * @since 1.0
 * @see IReporter
 * @see TestResultsRecords
 */
public class TestResultsReporter implements IReporter {

  /** Name of the JSON configuration file containing test plan and test case mappings. */
  private static final String TEST_PLAN_SUITE_FILE_NAME = "test-plan-suite.json";

  /** Path where the final test results report will be written. */
  private static final String TEST_CASE_RESULTS_FILE_PATH = "test-results/test-results-report.json";

  /** Logger instance for this class. */
  private static final Logger logger = LogManager.getLogger(TestResultsReporter.class);

  /** Map storing test method names to their corresponding test case information. */
  private static Map<String, TestCaseInfo> testCasesMap = new HashMap<>();

  /** Map storing aggregated test results by test case ID. */
  private static Map<String, TestResult> testResultsMap = new HashMap<>();

  /**
   * Generates the test results report by processing all test suites and their results.
   *
   * <p>This method is automatically called by TestNG after all tests have completed. It performs
   * the following operations:
   *
   * <ol>
   *   <li>Loads test case mappings from the configuration file
   *   <li>Processes all test results (passed, failed, skipped)
   *   <li>Aggregates results for parameterized tests
   *   <li>Generates and writes the final JSON report
   * </ol>
   *
   * @param xmlSuites list of XML suite configurations (not used in current implementation)
   * @param suites list of test suites containing execution results
   * @param outputDirectory directory where reports should be generated (not used, using fixed path)
   */
  @Override
  public void generateReport(
      List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

    // Load test case mappings from configuration file
    TestPlanSuite testPlanSuite =
        JsonUtils.fromJson(TEST_PLAN_SUITE_FILE_NAME, TestPlanSuite.class, true);
    if (testPlanSuite != null) {
      testCasesMap = testPlanSuite.testCases();
    }

    // Process all test results from all suites
    for (ISuite suite : suites) {
      Map<String, ISuiteResult> suiteResults = suite.getResults();
      for (ISuiteResult suiteResult : suiteResults.values()) {
        ITestContext testContext = suiteResult.getTestContext();
        collectTestResults(testContext.getPassedTests());
        collectTestResults(testContext.getFailedTests());
        collectTestResults(testContext.getSkippedTests());
      }
    }

    // Create the final test result report
    TestResultReport testResultReport = null;
    if (testPlanSuite != null) {
      testResultReport =
          new TestResultReport(
              testPlanSuite.testPlanName(), testPlanSuite.testSuiteName(), testResultsMap);
    } else {
      // Fallback when configuration is not available
      testResultReport =
          new TestResultReport("Unknown Test Plan", "Unknown Test Suite", testResultsMap);
    }

    ensureOutputDirectory();
    writeResultsReport(testResultReport);
  }

  /**
   * Converts test method parameters to a string representation for logging and reporting.
   *
   * <p>This method filters out null parameters and {@link ITestContext} instances to create a clean
   * string representation of test parameters. This is particularly useful for parameterized tests
   * where we want to track what data was used.
   *
   * @param params array of test method parameters (may contain nulls)
   * @return string representation of non-null, non-ITestContext parameters, or empty string if none
   */
  private String getParametersAsString(Object[] params) {
    String stringParams = "";
    if (params != null && params.length > 0) {
      // Filter out null values and ITestContext parameters
      params =
          Arrays.stream(params)
              .filter(param -> param != null && !(param instanceof ITestContext))
              .toArray();

      if (params.length > 0) {
        stringParams = Arrays.toString(params);
      }
    }
    return stringParams;
  }

  /**
   * Converts TestNG status code to readable string.
   *
   * <p>Maps TestNG's internal status codes to human-readable outcome strings for better readability
   * in reports and logs.
   *
   * @param status the TestNG result status code from {@link ITestResult}
   * @return human-readable outcome string ("Passed", "Failed", "Skipped", or "Unknown")
   * @see ITestResult#SUCCESS
   * @see ITestResult#FAILURE
   * @see ITestResult#SKIP
   */
  private String getOutcomeString(int status) {
    return switch (status) {
      case ITestResult.SUCCESS -> "Passed";
      case ITestResult.FAILURE -> "Failed";
      case ITestResult.SKIP -> "Skipped";
      default -> "Unknown";
    };
  }

  /**
   * Calculates the next iteration ID for a parameterized test case.
   *
   * <p>For parameterized tests that run multiple times with different data sets, this method
   * ensures each iteration gets a unique, sequential ID within the same test case.
   *
   * @param testCaseId the test case identifier
   * @return the next available iteration ID (starting from 1)
   */
  private int getNextIterationId(String testCaseId) {
    TestResult existingResult = testResultsMap.get(testCaseId);
    return existingResult != null ? existingResult.iterationDetails().size() + 1 : 1;
  }

  /**
   * Processes and collects test results from a TestNG result map.
   *
   * <p>This method handles both single execution tests and parameterized tests with multiple
   * iterations. For parameterized tests, it creates individual iteration records while maintaining
   * an overall test result summary.
   *
   * <h4>Processing Logic:</h4>
   *
   * <ul>
   *   <li>Calculates execution duration (endTime - startTime)
   *   <li>Maps test method names to test case IDs using configuration
   *   <li>For parameterized tests: creates iteration records with sequential IDs
   *   <li>For simple tests: creates single result records
   *   <li>Aggregates results by test case ID for reporting
   * </ul>
   *
   * @param resultMap TestNG result map containing test execution results
   * @see IResultMap
   * @see TestIterationResult
   * @see TestResult
   */
  public void collectTestResults(IResultMap resultMap) {
    for (ITestResult testResult : resultMap.getAllResults()) {
      // Extract basic test information
      String outcome = getOutcomeString(testResult.getStatus());
      Long durationInMs = testResult.getEndMillis() - testResult.getStartMillis();
      String testName = testResult.getMethod().getMethodName();

      // Map test method to test case ID
      TestCaseInfo testCaseInfo = testCasesMap.get(testName);
      String testCaseId = testCaseInfo != null ? testCaseInfo.testCaseId() : "Unknown";
      String testParams = getParametersAsString(testResult.getParameters());

      String comment = "Test Name: " + testName;

      // Handle parameterized tests (tests with parameters)
      if (!testParams.isEmpty()) {
        TestIterationResult iterationResult =
            new TestIterationResult(
                getNextIterationId(testCaseId),
                outcome,
                "Test Parameters: " + testParams,
                durationInMs);

        TestResult existingResult = testResultsMap.get(testCaseId);
        if (existingResult != null) {
          // Add iteration to existing test result
          testResultsMap.put(testCaseId, existingResult.withIterationResult(iterationResult));
        } else {
          // First iteration for this test case
          TestResult newResult =
              new TestResult(outcome, comment, durationInMs, List.of(iterationResult));
          testResultsMap.put(testCaseId, newResult);
        }
      } else {
        // Handle simple tests (no parameters)
        TestResult newResult = new TestResult(outcome, comment, durationInMs, List.of());
        testResultsMap.put(testCaseId, newResult);
      }
    }
  }

  /**
   * Ensures the output directory exists for writing the test results report.
   *
   * <p>Creates the necessary directory structure if it doesn't exist. Logs success or failure of
   * directory creation for debugging purposes.
   *
   * @see #TEST_CASE_RESULTS_FILE_PATH
   */
  private void ensureOutputDirectory() {
    File outputFile = new File(TEST_CASE_RESULTS_FILE_PATH);
    File outputDir = outputFile.getParentFile();
    if (outputDir != null && !outputDir.exists()) {
      if (outputDir.mkdirs()) {
        logger.info("Created output directory: {}", outputDir.getAbsolutePath());
      } else {
        logger.error("Failed to create output directory: {}", outputDir.getAbsolutePath());
      }
    }
  }

  /**
   * Writes the test results report to a JSON file.
   *
   * <p>Converts the test result report record to a JSON format and writes it to the configured
   * output path. The JSON structure includes:
   *
   * <ul>
   *   <li>Test plan and suite metadata
   *   <li>Detailed test results by test case ID
   *   <li>Iteration details for parameterized tests
   *   <li>Execution durations and outcomes
   * </ul>
   *
   * @param report the complete test result report to write
   * @throws RuntimeException if file writing fails (logged as error)
   * @see JsonUtils#mapToJson(Map)
   */
  private void writeResultsReport(TestResultReport report) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_CASE_RESULTS_FILE_PATH))) {
      // Convert record to Map for JsonUtils compatibility
      Map<String, Object> reportMap =
          Map.of(
              "testPlanName", report.testPlanName(),
              "testSuiteName", report.testSuiteName(),
              "testResults", report.testResults());
      String json = JsonUtils.mapToJson(reportMap);
      writer.write(json);
      logger.info("Test results report generated successfully: {}", TEST_CASE_RESULTS_FILE_PATH);
    } catch (IOException e) {
      logger.error("Failed to generate Test Results Report: ", e);
    }
  }
}
