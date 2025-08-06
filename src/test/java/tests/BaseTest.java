package tests;

import io.restassured.RestAssured;
import java.lang.reflect.Method;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import utils.RestAssuredLogFilter;

public class BaseTest {
  private static final String BASE_URI = "http://localhost:3000";
  private static final String BASE_PATH = "/api/books";
  protected static final String USER_AUTH_TOKEN = "user-token";
  protected static final String USER_ADMIN_TOKEN = "admin-token";

  @BeforeSuite
  public void setUpSuite() {
    RestAssured.baseURI = BASE_URI;
    RestAssured.basePath = BASE_PATH;
    RestAssured.filters(new RestAssuredLogFilter());
  }

  @BeforeTest
  public void beforeTest(ITestContext context) {
    ThreadContext.put("testName", this.getClass().getSimpleName() + ".BeforeTest");
  }

  @BeforeMethod
  public void setup(Method method) {
    ThreadContext.put(
        "testName", method.getDeclaringClass().getSimpleName() + "." + method.getName());
  }

  @AfterMethod
  public void teardown() {
    ThreadContext.clearAll();
  }

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
}
