package tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;
import utils.RestAssuredLogFilter;

public class BaseTest {
  private static final String BASE_URI = "http://localhost:3000";
  private static final String BASE_PATH = "/api/books";
  protected static final String USER_AUTH_TOKEN = "user-token";

  @BeforeSuite
  public void setUpSuite() {
    RestAssured.baseURI = BASE_URI;
    RestAssured.basePath = BASE_PATH;
    RestAssured.filters(new RestAssuredLogFilter());
  }
}
