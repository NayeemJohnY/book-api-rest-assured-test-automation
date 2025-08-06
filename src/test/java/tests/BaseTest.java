package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utils.RestAssuredLogFilter;

/** Base test class for API tests, providing setup and teardown logic. */
public class BaseTest {
  private static final String BASE_URI = "http://localhost:3000";
  private static final String BASE_PATH = "/api/books";
  protected static final String USER_AUTH_TOKEN = "user-token";
  protected static final String USER_ADMIN_TOKEN = "admin-token";
  protected Logger logger = LogManager.getLogger(getClass());
  protected static final int MAX_RETRY_COUNT = 3;

  /** Sets up the test suite with base URI, path, and filters. */
  @BeforeSuite
  public void setUpSuite() {
    RestAssured.baseURI = BASE_URI;
    RestAssured.basePath = BASE_PATH;
    RestAssured.filters(new RestAssuredLogFilter());
  }

  /**
   * Runs before each test, sets up thread context.
   *
   * @param context the test context
   */
  @BeforeTest
  public void beforeTest() {
    ThreadContext.put("testName", this.getClass().getSimpleName() + ".BeforeTest");
  }

  /**
   * Runs before each test method, sets up thread context.
   *
   * @param method the test method
   */
  @BeforeMethod
  public void setup(Method method) {
    ThreadContext.put(
        "testName", method.getDeclaringClass().getSimpleName() + "." + method.getName());
  }

  /** Runs after each test method, clears thread context. */
  @AfterMethod
  public void teardown() {
    ThreadContext.clearAll();
  }

  /** Resets the books after the test suite completes. */
  @AfterSuite
  public void resetBooksAfterSuite() {
    RestAssured.given()
        .auth()
        .oauth2(USER_ADMIN_TOKEN)
        .when()
        .delete("/reset")
        .then()
        .statusCode(204);
  }

  /**
   * Retries a request if HTTP 429 is received, up to MAX_RETRY_COUNT.
   *
   * @param request the request supplier
   * @return the response
   */
  public Response retryRequest(Supplier<Response> request) {
    int retryCount = 0;
    Response response;
    do {
      response = request.get();
      if (response.getStatusCode() != 429) {
        return response;
      }
      retryCount++;

      String retryAfter = response.getHeader("Retry-After");
      int retryAfterSeconds = retryAfter != null ? Integer.parseInt(retryAfter) : 30;

      if (retryCount < MAX_RETRY_COUNT) {
        String message =
            "Retrying test after "
                + retryAfterSeconds
                + " seconds due to 429 Too Many Requests (attempt "
                + retryCount
                + ")";

        logger.info(message);
        try {
          Thread.sleep(retryAfterSeconds * 1000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }

    } while (retryCount < MAX_RETRY_COUNT);
    return response;
  }
}
