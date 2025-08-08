package testUtils;

import org.apache.logging.log4j.Logger;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * A Hamcrest matcher that logs assertion results using Log4j2.
 *
 * @param <T> the type of object being matched
 */
public class LoggingMatcher<T> extends TypeSafeMatcher<T> {

  private final Matcher<? super T> matcher;
  private final Logger logger;

  /**
   * Constructs a LoggingMatcher.
   *
   * @param logger the logger to use
   * @param matcher the matcher to delegate to
   */
  public LoggingMatcher(Logger logger, Matcher<? super T> matcher) {
    this.logger = logger;
    this.matcher = matcher;
  }

  /**
   * Describes the matcher to the given description.
   *
   * @param description the description to append to
   */
  @Override
  public void describeTo(Description description) {
    matcher.describeTo(description);
  }

  /**
   * Evaluates the matcher and logs the result.
   *
   * @param actual the actual value
   * @return true if the match is successful, false otherwise
   */
  @Override
  protected boolean matchesSafely(T actual) {
    boolean result = matcher.matches(actual);
    if (result) {
      logger.info("Assertion Passed: actual='{}', expected='{}'", actual, matcher.toString());
    } else {
      logger.error("Assertion Failed: actual='{}', expected='{}'", actual, matcher.toString());
    }
    return result;
  }

  /**
   * Factory method to create a LoggingMatcher.
   *
   * @param logger the logger to use
   * @param matcher the matcher to delegate to
   * @param <T> the type of object being matched
   * @return a new LoggingMatcher instance
   */
  public static <T> LoggingMatcher<T> log(Logger logger, Matcher<? super T> matcher) {
    return new LoggingMatcher<T>(logger, matcher);
  }
}
