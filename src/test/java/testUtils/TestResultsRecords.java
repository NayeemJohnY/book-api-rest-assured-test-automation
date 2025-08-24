/**
 * About test-results collection into a JSON:
 *
 * <p>This file defines Java records used for collecting and structuring test execution results. The
 * framework uses these records to aggregate test outcomes, durations, and parameters, and then
 * exports all results into a single JSON file (`test-results/test-results-report.json`) for
 * reporting and integration with external systems.
 */
package testUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Java records */
public class TestResultsRecords {

  /** Represents a test case info with testcaseId. */
  public record TestCaseInfo(String testCaseId) {}

  /** Represents a suite of test cases within a test plan. */
  public record TestPlanSuite(
      String testPlanName, String testSuiteName, Map<String, TestCaseInfo> testCases) {}

  /** Represents a single test iteration result. */
  public record TestIterationResult(int id, String outcome, String comment, Long durationInMs) {}

  /** Represents the complete test result for a test case. */
  public record TestResult(
      String outcome,
      String comment,
      Long durationInMs,
      List<TestIterationResult> iterationDetails) {

    /** Constructor for single test execution (no iterations). */
    public TestResult(String outcome, String comment, Long durationInMs) {
      this(outcome, comment, durationInMs, List.of());
    }

    /** Creates a new TestResult with an additional iteration. */
    public TestResult withIterationResult(TestIterationResult iterationResult) {
      ArrayList<TestIterationResult> iterationResults = new ArrayList<>(iterationDetails);
      iterationResults.add(iterationResult);

      // Calculate new total duration
      Long newDuration = durationInMs + iterationResult.durationInMs();

      // Determine overall outcome - if any iteration failed, mark as failed
      String newOutcome = outcome;
      if ("Failed".equals(outcome) || "Failed".equals(iterationResult.outcome())) {
        newOutcome = "Failed";
      }

      return new TestResult(newOutcome, comment, newDuration, iterationResults);
    }
  }

  /** Container for all test results. */
  public record TestResultReport(
      String testPlanName, String testSuiteName, Map<String, TestResult> testResults) {}
}
