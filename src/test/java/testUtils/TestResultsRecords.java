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
import java.util.Set;
import java.util.stream.Collectors;

/** Java records */
public class TestResultsRecords {

  /** Represents a test case info with testcaseId. */
  public record TestCaseInfo(String testCaseId) {}

  /** Represents a suite of test cases within a test plan. */
  public record TestPlanSuite(
      String testPlanName, String testSuiteName, Map<String, TestCaseInfo> testCases) {}

  /** Represents a single test iteration result. */
  public record TestIterationResult(
      int id, String outcome, String comment, Long durationInMs, String errorMessage) {}

  /** Represents the complete test result for a test case. */
  public record TestResult(
      String outcome,
      String comment,
      Long durationInMs,
      String errorMessage,
      List<TestIterationResult> iterationDetails) {

    /** Constructor for single test execution (no iterations). */
    public TestResult(String outcome, String comment, Long durationInMs, String errorMessage) {
      this(outcome, comment, durationInMs, errorMessage, List.of());
    }

    /** Creates a new TestResult with an additional iteration. */
    public TestResult withIterationResult(TestIterationResult iterationResult) {
      ArrayList<TestIterationResult> iterationResults = new ArrayList<>(iterationDetails);
      iterationResults.add(iterationResult);

      // Calculate new total duration
      Long newDuration = durationInMs + iterationResult.durationInMs();

      // Determine overall outcome
      Set<String> outcomes =
          iterationResults.stream().map(TestIterationResult::outcome).collect(Collectors.toSet());

      String newOutcome = "Inconclusive";
      if (outcomes.size() == 1) {
        String only = outcomes.iterator().next();
        if (only.equals("Passed") || only.equals("Failed") || only.equals("Error")) {
          newOutcome = only;
        }
      }

      String newErrorMessage = errorMessage;
      if (!iterationResult.errorMessage().isEmpty()) {
        newErrorMessage =
            "Iteration " + iterationResults.size() + ": " + iterationResult.errorMessage() + "\n";
        if (!errorMessage.isEmpty()) {
          newErrorMessage = errorMessage + "\n" + newErrorMessage;
        }
      }

      return new TestResult(newOutcome, comment, newDuration, newErrorMessage, iterationResults);
    }
  }

  /** Container for all test results. */
  public record TestResultReport(
      String testPlanName, String testSuiteName, Map<String, TestResult> testResults) {}
}
