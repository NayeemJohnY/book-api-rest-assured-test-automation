package testUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

/** Utility class for assertions with logging using Log4j2. */
public class Assertion {

  private static final Logger logger = LogManager.getLogger(Assertion.class);

  /**
   * Asserts that value is NOT NULL and logs the result.
   *
   * @param object the object to check
   * @param message the assertion message
   */
  public static void assertNotNull(Object object, String message) {
    assertWithLog(() -> Assert.assertNotNull(object), message);
  }

  /**
   * Asserts that two values are equal and logs the result.
   *
   * @param actual the actual value
   * @param expected the expected value
   * @param message the assertion message
   * @param <T> the type of the values
   */
  public static <T> void assertEquals(T actual, T expected, String message) {
    assertWithLog(() -> Assert.assertEquals(actual, expected), message, actual, expected);
  }

  /**
   * Runs an assertion and logs the result (pass/fail) with a message.
   *
   * @param assertion the assertion to run
   * @param message the assertion message
   */
  private static void assertWithLog(Runnable assertion, String message) {
    try {
      assertion.run();
      logger.info("Assertion Passed: '{}'", message);
    } catch (AssertionError e) {
      logger.error("Assertion Failed: '{}'", message);
      throw e;
    }
  }

  /**
   * Runs an assertion and logs the result (pass/fail) with a message and values.
   *
   * @param assertion the assertion to run
   * @param message the assertion message
   * @param actual the actual value
   * @param expected the expected value
   * @param <T> the type of the values
   */
  private static <T> void assertWithLog(Runnable assertion, String message, T actual, T expected) {
    try {
      assertion.run();
      logger.info("Assertion Passed: '{}', actual='{}', expected='{}'", message, actual, expected);
    } catch (AssertionError e) {
      logger.error("Assertion Failed: '{}', actual='{}', expected='{}'", message, actual, expected);
      throw e;
    }
  }
}
