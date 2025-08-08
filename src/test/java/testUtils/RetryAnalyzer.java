package testUtils;

import io.qameta.allure.Allure;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** Retry analyzer for TestNG tests to handle HTTP 429 responses. */
public class RetryAnalyzer implements IRetryAnalyzer {
  private int retryCount = 0;
  private final int maxRetryCount = 2;
  private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);

  /**
   * Retries the test if the status code is 429 (Too Many Requests) and retry count is not exceeded.
   *
   * @param result the test result
   * @return true if the test should be retried, false otherwise
   */
  @Override
  public boolean retry(ITestResult result) {
    Object statusCode = result.getAttribute("statusCode");
    if (statusCode instanceof Integer && (int) statusCode == 429 && retryCount < maxRetryCount) {
      retryCount++;
      Object retryAfter = result.getAttribute("retryAfter");
      int retryAfterSeconds = retryAfter instanceof Integer ? (int) retryAfter : 30;

      String message =
          "Retrying test after "
              + retryAfterSeconds
              + " seconds due to 429 Too Many Requests (attempt "
              + retryCount
              + ")";

      logger.warn(message);
      Allure.step(message);
      try {
        Thread.sleep(retryAfterSeconds * 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }
}
